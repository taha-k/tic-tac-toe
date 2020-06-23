(ns tic-tac-toe.routes
  (:require [compojure.api.core]
            [compojure.route :as route]
            [compojure.api.sweet :refer :all]
            [clojure.spec.alpha :as spec]
            [compojure.api.coercion.spec :as spec-coercion]
            [compojure.api.exception]
            [ring.middleware.defaults :refer :all]
            [clojure.tools.logging :as logging]
            [spec-tools.core :as spec-tools]
            [spec-tools.transform :as transform]
            [ring.util.response :refer [response not-found]]
            [tic-tac-toe.game.routes :as game-routes]
            [ring.util.http-response :refer [ok] :as http-response]))

(defn validation-handler [status-code]
  (fn [e data _req]
    (logging/errorf e "Validation error: %s" data)
    (let [validation-data {::spec/problems (:problems data)}]
      {:status status-code
       :body   (with-out-str (spec/explain-out validation-data))})))

(defn not-found-exception-handler [e data _req]
  (logging/error e (.getMessage e))
  (http-response/not-found {:type  "not-found"
                            :error (:error data)}))

(defn bad-request-exception-handler [e data _req]
  (logging/error e (.getMessage e))
  (http-response/bad-request {:type  "bad-request"
                              :error (:error data)}))

(defn unprocessable-entity-exception-handler [_e data _req]
  (http-response/unprocessable-entity {:type  "unprocessable-entity"
                                       :error (:error data)}))

(def conforming-json-coercion (spec-tools/type-transformer
                                {:decoders
                                 (merge
                                   transform/json-type-decoders
                                   transform/strip-extra-keys-type-decoders)}))

(def conforming-spec-coercion (spec-coercion/create-coercion
                                (assoc-in spec-coercion/default-options
                                          [:body :formats "application/json"]
                                          conforming-json-coercion)))

(defn tic-tac-toe-api []
  (api {:coercion   conforming-spec-coercion
        :exceptions {:handlers {:compojure.api.exception/request-validation  (validation-handler 400)
                                :compojure.api.exception/response-validation (validation-handler 500)
                                :not-found                                   not-found-exception-handler
                                :bad-request                                 bad-request-exception-handler
                                :unprocessable-entity                        unprocessable-entity-exception-handler}}}
       (swagger-routes {:ui   "/api-docs"
                        :data {:info {:title "Tic Tac Toe Rest API"
                                      :description "Attach your client and play tic-tac-toe"}}})
       (routes
         (context "/" []
           (GET "/health" []
             :summary "Check if alive"
             (ok  "It is ALIVE!"))
           (game-routes/game-routes)
           (undocumented
                   (route/not-found "<h1>Resource not found</h1>"))))))