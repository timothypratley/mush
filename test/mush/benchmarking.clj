(ns mush.benchmarking
  (:require
    [clojure.test :refer :all]
    [clojure.java.io :as io]
    [clojure.string :as string]
    [criterium.core :as cr]
    [clj-jgit.porcelain :as porc]
    [clj-jgit.querying :as q]
    [camel-snake-kebab.core :as kebab]))

(defn format-time [t]
  (apply cr/format-value t (cr/scale-time t)))

(def nsecs 1e9)
(def msecs 1000)

(def git-commit-info
  (porc/with-repo "." (some-> (q/rev-list repo)
                              (first)
                              (->> (q/commit-info repo))
                              (dissoc :raw :repo))))

(def benchmarks
  (atom {(:time git-commit-info) {:git-commit-info git-commit-info}}))

(defn add-results! [k results]
  (swap! benchmarks assoc-in [(:time git-commit-info) k] results))

(def benchmark-filename
  (str (string/replace (:time git-commit-info) " " "_")
       "-" (:id git-commit-info) ".edn"))

(defn save-gathered-benchmarks [f]
  (f)
  (with-open [w (io/writer (doto (io/file "benchmarks" benchmark-filename)
                             (io/make-parents)))]
    (clojure.pprint/pprint @benchmarks w)))

(defmacro bench-press [title acceptable-time & form]
  `(deftest ~(symbol (kebab/->kebab-case title))
     (testing ~title
       (let [results# (assoc (cr/quick-benchmark ~@form nil)
                        :acceptable-time ~acceptable-time)]
         (add-results! ~title results#)
         (is (< (first (:mean results#)) ~acceptable-time))))))
