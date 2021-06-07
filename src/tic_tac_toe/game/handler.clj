(ns tic-tac-toe.game.handler
  (:require [tic-tac-toe.game.storage :refer [store]]
            [clojure.string :as str]
            [clojure.set :as cset]
            [clojure.tools.logging :as logging]
            [clojure.spec.alpha :as s]
            [tic-tac-toe.spec.game :as game-spec]))

(defn- next-player? [turn-count]
  (if (even? turn-count) "X" "O"))

(defn- get-game [game-id]
  (try
    (-> @store (nth (dec game-id)))
    (catch IndexOutOfBoundsException e
      (throw (ex-info "Game not found"
                      {:type     :not-found
                       :error    (format "Invalid game-id %s" game-id)})))))


(defn- is-valid-turn? [current-turns next-player]
  (or (and (even? (count current-turns))
           (= next-player "X"))
      (and (odd? (count current-turns))
           (= next-player "O"))))

(defn- check-rows-columns [player-turns]
  (->> ["0" "1" "2"]
       (map (fn [value]
              (or (as-> player-turns $
                        (filter #(str/starts-with? % value) $)
                        (= 3 (count $)))
                  (as-> player-turns $
                        (filter #(str/ends-with? % value) $)
                        (= 3 (count $))))))
       (some true?)))

(defn- check-diagonals [player-turns]
  (or (cset/subset? #{"00" "11" "22"} (set player-turns))
      (cset/subset? #{"02" "11" "20"} (set player-turns))))

(defn- win-condition [player-turns player]
  (when (or (check-rows-columns player-turns)
            (check-diagonals player-turns))
    player))

(defn- calculate-status [turns]
  (if-let [won (if (odd? (count turns))
                 (win-condition (take-nth 2 turns) "X")
                 (win-condition (take-nth 2 (rest turns)) "O"))]
    won
    (when (= (count turns) 9)
      "Draw")))

(defn start-game []
  (let [game-id  (-> @store count inc)]
    (swap! store #(conj % {:game-id game-id :turns []}))
    {:gameId game-id :nextPlayer (next-player? 0)}))

(defn mark [game-id {:keys [location player]}]
  (let [{turns :turns status :status :as game} (get-game game-id)]
    (when status
      (throw (ex-info "Status is already defined, game has ended"
                      {:type     :unprocessable-entity
                       :error    (format "This game has been completed with status=%s" status)})))

    (when (and player
               (not (is-valid-turn? turns player)))
      (throw (ex-info (format "Wrong player played turn")
                      {:type     :unprocessable-entity
                       :error    (format "Incorrect turn played")})))

    (when ((set turns) location)
      (throw (ex-info (format "Location is already marked")
                      {:type     :unprocessable-entity
                       :error    (format "Location is already marked")})))

    (let [turns (conj turns location)
          status (when (> (count turns) 4)
                   (calculate-status turns))]
      (swap! store #(assoc-in % [(dec game-id)] (cond-> game
                                                        true (assoc :turns turns)
                                                        status (assoc :status status))))

      (cond-> {:gameId game-id}
              (nil? status) (assoc :nextPlayer (-> turns count next-player?))
              status (assoc :status status)))))

(defn get-status [game-id]
  (let [{status :status} (get-game game-id)]
    (if status
      {:status status}
      {:status "Ongoing"})))

(s/fdef start-game
        :ret ::game-spec/start-response)

(s/fdef mark
        :args (s/cat :game-id ::game-spec/gameId
                     :body ::game-spec/body)
        :ret ::game-spec/mark-response)

(s/fdef get-status
        :args (s/cat :game-id ::game-spec/gameId)
        :ret ::game-spec/status-response)
