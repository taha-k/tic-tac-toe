(ns tic-tac-toe.handlers
  (:require [clojure.pprint :as pp]
            [clojure.string :as str]
            [ring.util.http-response :refer :all]))

; Simple Body Page
(defn simple-body-page [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "Hello World"})

(defn health [req]
  (ok "it is alive"))

; request-example
(defn request-example [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    (->> (pp/pprint req)
                 (str "Request Object: " req))})