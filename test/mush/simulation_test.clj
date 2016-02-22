(ns ^:perf mush.simulation-test
  (:require
    [clojure.test :refer :all]
    [mush.simulation :as sim]
    [mush.benchmarking :as b]))

(use-fixtures :once b/save-gathered-benchmarks)

(deftest slow-test
  (is (< (b/time-msecs (sim/sim-project sim/project1-bad))
         1000)
      "A full project simulation should take less than one second"))

(deftest slow-test-2
  (with-redefs [sim/completion-rate sim/completion-rate-better]
    (is (< (b/time-msecs (sim/sim-project sim/project1-bad))
           1)
        "A full project simulation should take less than one second")))


(deftest bad-microbenchmark
  (time (inc 1)))

(b/bench-press "Remove 1 done story" b/msec
  (sim/remove-done-stories sim/project1-bad))

(b/bench-press "Bug?" b/msec
  (sim/bug? "When the user clicks new story, the app crashes. (bug)"))

(def stories
  ["When the user clicks new story, the app crashes. (bug)"
   "Add a team member profile page. (feature)"
   "Build a backend service to store stories."
   "Every second story created splits into three stories. (bug)"])

(b/bench-press "Count stories that satisfy bug?" b/msec
  (count (filter sim/bug? stories)))

(b/bench-press "Count stories that satisfy bug??" b/msec
  (count (filter sim/bug?? stories)))
