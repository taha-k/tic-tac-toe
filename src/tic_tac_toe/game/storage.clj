(ns tic-tac-toe.game.storage
  (:require [mount.core :as mount :refer [defstate]]
            [clojure.tools.logging :as logging]))

(def ^:const initial-state [[nil nil nil]
                            [nil nil nil]
                            [nil nil nil]])

(defstate store :start (atom []))

(defn mount-state []
  (logging/info "Mounting storage")
  (mount/start #'tic-tac-toe.game.storage/store))
