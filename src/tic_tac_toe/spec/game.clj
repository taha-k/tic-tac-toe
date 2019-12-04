(ns tic-tac-toe.spec.game
  (:require [clojure.spec.alpha :as s]))

(s/def ::gameId pos-int?)
(s/def ::player #{"X" "O"})
(s/def ::nextPlayer ::player)
(s/def ::status #{"X" "O" "Draw" "Ongoing"})

(s/def ::start-response (s/keys :req-un [::gameId ::nextPlayer]))

(s/def ::mark-response-completed (s/keys :req-un [::gameId ::status]))
(s/def ::mark-response (s/or :ongoing ::start-response
                             :completed ::mark-response-completed))

(s/def ::status-response (s/keys :req-un [::status]))


(s/def ::location #{"00" "01" "02"
                    "10" "11" "12"
                    "20" "21" "22"})

(s/def ::body (s/keys :req-un [::location]
                      :opt-un [::player]))
