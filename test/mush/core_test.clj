(ns mush.core-test
  (:require
    [clojure.test :refer :all]
    [mush.core :as mush]
    [criterium.core :as cr]))

(defn format-time [t]
  (apply cr/format-value t (cr/scale-time t)))

(def nsecs 1e9)

(deftest a-test
  (testing "stuff"
    (let [results (-> (cr/quick-benchmark (mush/foo 1) nil))]
      (spit "benchmarks.edn" results)
      (prn "HAHAHA" (format-time (first (:mean results)))
           (map format-time (second (:mean results))))
      (is (< (* 1000 nsecs) (first (:mean results)))))))
