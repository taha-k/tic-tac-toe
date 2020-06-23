(ns tic-tac-toe.game.routes
  (:require [compojure.api.sweet :refer [context POST GET]]
            [ring.util.response :refer [response not-found]]
            [tic-tac-toe.spec.game :as game-spec]
            [tic-tac-toe.swagger :as swagger]
            [tic-tac-toe.game.handler :as handler]))

(defn game-routes []
  (context "/game" []
    :tags ["game"]

    (POST "/start" []
      :return ::game-spec/start-response
      :summary "Starts a new game by returning the gameId"
      :swagger (swagger/annotation "game/start")
      (response (handler/start-game)))

    (POST "/:game-id/mark" []
      :path-params [game-id :- ::game-spec/gameId]
      :body [body ::game-spec/body]
      :responses {200 {:schema ::game-spec/mark-response
                       :description "Returns current state of the game"}
                  400 {:schema      {:message string?}
                       :description "Bad input"}
                  404 {:schema      {:message string?}
                       :description "Game not found"}
                  422 {:schema      {:message string?}
                       :description "Invalid inputs"}}
      :swagger (swagger/annotation "game/:gameId/mark")
      (response (handler/mark game-id body)))

    (GET "/:game-id/status" []
      :path-params [game-id :- ::game-spec/gameId]
      :summary "Returns the current status of a game. X: X won, O: O won, Draw or Ongoing"
      :return ::game-spec/status-response
      :swagger (swagger/annotation "game/:gameId/status")
      (response (handler/get-status game-id)))))
