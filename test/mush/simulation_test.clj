(ns mush.simulation-test
  (:require
    [clojure.test :refer :all]
    [mush.simulation :refer-all]
    [mush.benchmarking :as b]))

(use-fixtures :once b/save-gathered-benchmarks)

(deftest slow-tests
  (is (< (b/time-msecs (sim-project project1))
         1000)
      "A full project simulation should take less than one second"))

(deftest bad-microbenchmark
  (time (inc 1)))

(b/bench-press "Remove 1 done story" (* 1000 b/nsecs)
               (remove-done-stories project1))
