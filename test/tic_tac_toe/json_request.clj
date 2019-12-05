(ns tic-tac-toe.json-request
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock-request]
            [cheshire.core :as cheshire]
            [clojure.java.io :as io]
            [clojure.string :as string]))

(defn- with-json-content-type [request]
  (mock-request/content-type request "application/json"))

(defn json-post-request
  "Creates mock request with given url headers and data which is serialized to json body.
  Note! This request contains mutable state and cannot be reused between api calls."
  [url headers data]
  (-> (mock-request/request :post url (cheshire/generate-string data))
      (update :headers merge headers)
      with-json-content-type))

(defn json-get-request
  [url]
  (-> (mock-request/request :get url)
      with-json-content-type))

(defn json-delete-request
  [url headers]
  (-> (mock-request/request :delete url)
      (update :headers merge headers)
      with-json-content-type))

(defn parse-json-body [body]
  (cheshire/parse-string (slurp body) true))