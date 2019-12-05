(ns tic-tac-toe.game.handler-test
  (:require [clojure.test :refer :all]
            [tic-tac-toe.game.handler :as handler]
            [tic-tac-toe.game.storage :refer [store] :as storage]
            [mount.core :as mount]
            [clojure.tools.logging :as logging])
  (:import (clojure.lang ExceptionInfo)))

(try (use-fixtures :each
                   #((storage/mount-state) (%) (mount/stop)))
     (catch Exception e (logging/info "Some exceptions")))

(deftest game-start-handler-test
  (testing "Starts a game by incrementing game-ids ->"
    (doseq [id (range 0 3)]
      (let [{game-id :gameId next-player :nextPlayer} (handler/start-game)]
        (testing "Start a game"
          (is (= game-id (inc id)))
          (is (= next-player "X"))
          (is (count @store) game-id))))))

(deftest game-mark-handler-test
    (testing "X wins horizontally ->"
      (let [{game-id :gameId} (handler/start-game)]
        (doseq [move ["00" "10" "01" "20"]]
          (let [{next-player :nextPlayer} (handler/mark game-id {:location move})]
            (testing "Make a mark"
              (is (= (-> @store first :turns last) move))
              (is (= (if (even? (-> @store first :turns count))
                       "X" "O") next-player)))))

        (testing "get-status should be Ongoing"
          (let [{status :status} (handler/get-status game-id)]
            (is (= status "Ongoing"))))

        (testing "should result in X wins"
          (let [last-move "02"
                {next-player :nextPlayer status :status} (handler/mark game-id {:location last-move})]
            (is (= (-> @store first :turns last) last-move))
            (is (= status "X"))
            (is (nil? next-player))))

        (testing "get-status should be X"
          (let [{status :status} (handler/get-status game-id)]
            (is (= status "X"))))

        (testing "should throw as making next move is not possible"
          (let [last-move "11"]
            (is (thrown-with-msg? ExceptionInfo #"Status is already defined, game has ended"
                                  (handler/mark game-id {:location last-move})))
            (is (not= (-> @store first :turns last) last-move))))))

    (testing "O wins diagonally ->"
      (let [{game-id :gameId} (handler/start-game)]
        (doseq [move ["00" "02" "01" "11" "12"]]
          (let [{next-player :nextPlayer} (handler/mark game-id {:location move})]
            (testing "Make a mark"
              (is (= (-> @store (nth (dec game-id)) :turns last) move))
              (is (= (if (even? (-> @store (nth (dec game-id)) :turns count))
                       "X" "O") next-player)))))

        (testing "should result in O wins"
          (let [last-move "20"
                {next-player :nextPlayer status :status} (handler/mark game-id {:location last-move})]
            (is (= (-> @store (nth (dec game-id)) :turns last) last-move))
            (is (= status "O"))
            (is (nil? next-player))))

        (testing "get-status should be O"
          (let [{status :status} (handler/get-status game-id)]
            (is (= status "O"))))

        (testing "should throw as making as next move is not possible"
          (let [last-move "11"]
            (is (thrown-with-msg? ExceptionInfo #"Status is already defined, game has ended"
                                  (handler/mark game-id {:location last-move})))
            (is (not= (-> @store (nth (dec game-id)) :turns last) last-move))))))

    (testing "Draw ->"
      (let [{game-id :gameId} (handler/start-game)]
        (doseq [move ["00" "01" "02" "10" "20" "11" "21" "22"]]
          (let [{next-player :nextPlayer} (handler/mark game-id {:location move})]
            (testing "Make a mark"
              (is (= (-> @store (nth (dec game-id)) :turns last) move))
              (is (= (if (even? (-> @store (nth (dec game-id)) :turns count))
                       "X" "O") next-player)))))

        (testing "should throw as repeating a move is not allowed"
          (let [repeat-move "11"]
            (is (thrown-with-msg? ExceptionInfo #"Location is already marked"
                                  (handler/mark game-id {:location repeat-move})))
            (is (not= (-> @store (nth (dec game-id)) :turns last) repeat-move))))

        (testing "should throw if wrong player is playing turn"
          (let [move "12"]
            (is (thrown-with-msg? ExceptionInfo #"Wrong player played turn"
                                  (handler/mark game-id {:location move :player "O"})))
            (is (not= (-> @store (nth (dec game-id)) :turns last) move))))

        (testing "should result in Draw"
          (let [last-move "12"
                {next-player :nextPlayer status :status} (handler/mark game-id {:location last-move})]
            (is (= (-> @store (nth (dec game-id)) :turns last) last-move))
            (is (= status "Draw"))
            (is (nil? next-player))))

        (testing "get-status should be Draw"
          (let [{status :status} (handler/get-status game-id)]
            (is (= status "Draw"))))))

    (testing "Throws Not found"
      (is (thrown-with-msg? ExceptionInfo #"Game not found"
                            (handler/mark 5 {:location "22"})))))

