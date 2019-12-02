(ns tic-tac-toe.app
  (:require [compojure.core :refer :all]
            [ring.middleware.defaults :refer :all]
            [tic-tac-toe.routes]))

(defn application []
  (wrap-defaults #'tic-tac-toe.routes/app-routes site-defaults))
