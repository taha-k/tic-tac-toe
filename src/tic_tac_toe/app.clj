(ns tic-tac-toe.app
  (:require [tic-tac-toe.routes :as routes]
            [tic-tac-toe.json :refer [add-cheshire-encoders!]]))

(defn application []
  (do
    (add-cheshire-encoders!)
    (routes/tic-tac-toe-api)))
