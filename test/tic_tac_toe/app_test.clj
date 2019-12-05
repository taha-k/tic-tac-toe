(ns tic-tac-toe.app-test
  (:require [clojure.test :refer :all]
            [tic-tac-toe.app :as app]
            [tic-tac-toe.routes :as routes]
            [spy.core :as spy]))

(deftest app-test
  (let [mock-root-api (spy/stub true)]
    (with-redefs [routes/tic-tac-toe-api mock-root-api]
      (testing "Initiates the routes"
        (app/application)
        (is (spy/called? mock-root-api))))))
