(ns tic-tac-toe.json
  (:require [cheshire.generate :refer [add-encoder]])
  (:import (java.time LocalDate)
           (com.fasterxml.jackson.core JsonGenerator)))

(defn add-cheshire-encoders! []
  (add-encoder LocalDate (fn [^LocalDate date ^JsonGenerator gen]
                           (.writeString gen (.toString date))))
  (add-encoder Class (fn [^Class cls ^JsonGenerator gen]
                       (.writeString gen (.toString cls)))))