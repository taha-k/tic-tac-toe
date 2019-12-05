(ns tic-tac-toe.game.routes-test
  (:require [clojure.test :refer :all]
            [tic-tac-toe.routes :as routes]
            [tic-tac-toe.game.routes :as game-routes]
            [tic-tac-toe.game.handler :as handler]
            [tic-tac-toe.spec.game :as game-spec]
            [clojure.spec.gen.alpha :as gen]
            [clojure.spec.alpha :as s]
            [spy.core :as spy]
            [tic-tac-toe.json-request :as json-request]))

(deftest game-validity-routes-test
  (testing "Requests and responses are correct with valid specs ->"
    (testing "Happy paths ->"
      (let [mock-start-game (spy/stub (gen/generate (s/gen ::game-spec/start-response)))
            mock-mark (spy/stub (gen/generate (s/gen ::game-spec/mark-response)))
            mock-get-status (spy/stub (gen/generate (s/gen ::game-spec/status-response)))]
        (with-redefs [handler/start-game mock-start-game
                      handler/mark mock-mark
                      handler/get-status mock-get-status]
          (let [api (routes/tic-tac-toe-api)]
            (testing "GET /game/start"
              (let [request (json-request/json-get-request "/game/start")
                    response (api request)]
                (is (= 200 (:status response)))))

            (testing "POST /game/:game-id/mark"
              (let [request (json-request/json-post-request "/game/1/mark" {} {:location "11"})
                    response (api request)]
                (is (= 200 (:status response)))))

            (testing "GET /game/:game-id/status"
              (let [request (json-request/json-get-request "/game/1/status")
                    response (api request)]
                (is (= 200 (:status response)))))))))

    #_(testing "Sad paths ->"
        (testing "Invalid return types throw 500 ->"
          (let [mock-start-game (spy/stub {:game-id "a"})
                mock-mark (spy/stub {:some-stuff "as"})]
            (with-redefs [handler/start-game mock-start-game
                          handler/mark mock-mark]
              (let [api (routes/tic-tac-toe-api)]
                (testing "GET /game/start"
                  (let [request (json-request/json-get-request "/game/start")
                        response (api request)]
                    (is (= 500 (:status response)))))

                (testing "POST /game/:game-id/mark"
                  (let [request (json-request/json-post-request "/game/1/mark" {} {:location "11"})
                        response (api request)]
                    (is (= 500 (:status response)))))))))

        (testing "Invalid requests throw 400 ->"
          (let [mock-mark (spy/stub {:some-stuff "as"})]
            (with-redefs [handler/mark mock-mark]
              (let [api (routes/tic-tac-toe-api)]
                (testing "POST /game/:game-id/mark"
                  (let [request (json-request/json-post-request "/game/1/mark" {} {:location "32"})
                        response (api request)]
                    (is (= 400 (:status response))))))))))))

