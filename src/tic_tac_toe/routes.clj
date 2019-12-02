(ns tic-tac-toe.routes
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer :all]
            [tic-tac-toe.handlers :as handlers]))

(defroutes app-routes
           (GET "/" [] handlers/simple-body-page)
           (GET "/health" [] handlers/health)
           (GET "/request" [] handlers/request-example)
           (route/not-found "Error, page not found!"))