(ns tic-tac-toe.swagger)

(defn annotation [operation-id]
  {:x-name (keyword operation-id)
   :operationId operation-id})
