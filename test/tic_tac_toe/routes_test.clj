(ns tic-tac-toe.routes-test
  (:require [clojure.test :refer :all]
            [tic-tac-toe.routes :as routes]
            [tic-tac-toe.json-request :as json-request]))

(deftest routes-test
  (testing "Routes ->"
    (let [api (routes/tic-tac-toe-api)]
      (testing "GET /health"
        (let [request (json-request/json-get-request "/health")
              response (api request)]
          (is (= 200 (:status response)))
          (is (= "It is ALIVE!" (:body response)))))

      (testing "GET /api-docs is found"
        (let [request (json-request/json-get-request "/api-docs")
              response (api request)]
          (is (= 302 (:status response)))))

      (testing "Invalid endpoint returns 404"
        (let [request (json-request/json-get-request "/health2")
              response (api request)]
          (is (= 404 (:status response)))
          (is (= "<h1>Resource not found</h1>" (:body response))))))))
        