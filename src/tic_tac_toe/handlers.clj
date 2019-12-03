(ns tic-tac-toe.handlers
  (:require [clojure.pprint :as pp]
            [clojure.string :as str]
            [ring.util.response :refer [response not-found]]
            [ring.util.http-response :refer [ok]]))

; request-example
(defn request-example [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    (->> (pp/pprint req)
                 (str "Request Object: " req))})