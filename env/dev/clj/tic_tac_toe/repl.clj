(ns tic-tac-toe.repl
  (:require [org.httpkit.server :as http-kit]
            [clojure.tools.namespace.repl :refer [refresh]]
            [tic-tac-toe.app :refer :all]
            [environ.core :refer [env]]))

(defn port [] (Integer/parseInt (env :port "8089")))

(defonce server (atom nil))

(defn start-server
  "used for starting the server in development mode from REPL"
  ([] (start-server (port)))
  ([port]
   (reset! server (http-kit/run-server (application)
                                       {:port port}))
   (println "Server started on port" port)))

(defn stop-server []
  (@server)
  (reset! server nil))

(defn reset
  "Stops the system, reloads modified source files, and restarts it."
  []
  (stop-server)
  (alter-var-root #'server (constantly nil))
  (refresh :after 'tic-tac-toe.repl/start-server))
