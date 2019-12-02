(ns tic-tac-toe.main
  (:require [org.httpkit.server :as http-kit]
            [clojure.tools.logging :as logging]
            [environ.core :refer [env]]
            [tic-tac-toe.app :refer [application]])
  (:gen-class))

(defn port [] (Integer/parseInt (env :port "8089")))

(defn -main []
  (logging/info "Application starting with environment:")
  (http-kit/run-server (application) {:port (port)}))
