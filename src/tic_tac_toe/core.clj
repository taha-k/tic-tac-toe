(ns tic-tac-toe.core
  (:require [org.httpkit.server :as server]
            [compojure.core :refer :all]
            [ring.middleware.defaults :refer :all]
            [clojure.string :as str]
            [clojure.data.json :as json]
            [environ.core :refer [env]]
            [tic-tac-toe.routes])
  (:gen-class))

(defn port [] (Integer/parseInt (env :port "8089")))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [port (port)]
    ; Run the server with Ring.defaults middleware
    (server/run-server (wrap-defaults #'tic-tac-toe.routes/app-routes site-defaults) {:port port})
    ; Run the server without ring defaults
    ;(server/run-server #'app-routes {:port port})
    (println (str "Running webserver at http:/127.0.0.1:" port "/"))))
