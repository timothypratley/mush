(ns mush.core-test
  (:require
    [clojure.test :refer :all]
    [clojure.java.io :as io]
    [clojure.string :as string]
    [mush.benchmarking :as b]
    [mush.core :as mush]
    [criterium.core :as cr]
    [clj-jgit.porcelain :as porc]
    [clj-jgit.querying :as q]
    [camel-snake-kebab.core :as kebab]))

(use-fixtures :once b/save-gathered-benchmarks)

(b/bench-press "stuff" (* 1000 b/nsecs)
               (mush/foo 1))
