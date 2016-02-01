(ns mush.core
  (:require [clojure.string :as string])
  (:import (java.util Date)))

(def project1
  {:title "Build a task tracking solution"
   :team '("timothypratley@gmail" "jeremy" "x@y")
   :stories '({:id "aaa"
               :title "Build a ClojureScript front end"
               :status "Ready"
               :as-of (Date.)}
               {:id "bbb"
                :title "Build backend services"
                :status "Done"
                :as-of (Date.)}
               {:id "ccc"
                :title "Tune performance"
                :status "In Progress"
                :as-of (Date.)})})

(def buzz-words
  ["create" "implement" "refactor" "tune" "resize" "load" "verify" "update"
   "widget" "feature" "wizard" "sorting" "search" "crud" "ui" "dashboard" "history" "configuration" "dropdown"
   "deploy" "password" "authentication" "performance" "monitoring" "stream" "social" "flat" "experience"
   "and" "or" "but" "with" "then" "that" "who" "why" "when" "because" "a"
   "firefox" "ie" "osx" "windows" "disk" "space" "crash" "freeze" "blank"])

(defn fetch-history [member]
  (Thread/sleep 10)
  {:some "data"})

(defn fetch-profile [member]
  (Thread/sleep 11)
  {:more "data"})

(defn member-info [member]
  (let [history (fetch-history member)
        profile (fetch-profile member)]
    (merge history profile)))

(defn stories-by-status [{:keys [stories]} status]
  (filter #(= (:status %) status) stories))

(defn unique []
  (Integer/toString (rand-int (Math/pow 36 3)) 36))

(defn add-story [project]
  (update project :stories conj
          {:id (unique)
           :title (string/capitalize (string/join " " (take (+ 3 (rand-int 9)) (shuffle buzz-words))))
           :status "Ready"
           :as-of (Date.)}))

(defn remove-story [project story]
  (update project :stories
          (fn [stories]
            (remove #(= (:id %) (:id story)) stories))))

(defn update-story-status [project story status]
  (update project :stories
          (fn [stories]
            (for [s stories]
              (if (= (:id s) (:id story))
                (assoc s :status status)
                s)))))

(defn discover-new-stories [project]
  (nth (iterate add-story project) (rand-int 2)))

(defn start-story [project story]
  (-> project
    (update-story-status (:id story) "In Progress")
    (discover-new-stories)))

(defn start-stories [project]
  (let [team-count (count (:team project))
        doing-count (count (stories-by-status project "In Progress"))]
    (reduce start-story project
            (take (- team-count doing-count) (stories-by-status project "Ready")))))

(defn completion-rate [project]
  (reduce (fn [acc member]
            (+ acc (:stories-per-day (member-info member) 0.5)))
          0
          (:team project)))

(defn complete-stories [project]
  (reduce (fn complete-story [acc story]
            (update-story-status acc story "Done"))
          project
          (take (rand-int (int (* (completion-rate project) 2)))
                (stories-by-status project "In Progress"))))

(defn standup [project]
  (-> project
    (complete-stories)
    (start-stories)))

(defn remove-done-stories [project]
  (reduce remove-story project (stories-by-status project "Done")))

(defn new-requirements [project]
  (nth (iterate add-story project) (count (:team project))))

(defn prioritize [project]
  (reduce remove-story project
          (drop (* 3 (count (:team project)))
                (shuffle (stories-by-status project "Ready")))))

(defn planning [project]
  (-> project
    (remove-done-stories)
    (new-requirements)
    (prioritize)))

(defn sim-week [project]
  (planning (nth (iterate standup project) 5)))

(defn sim-project [project]
  (nth (iterate sim-week project) 20))


(defn dont-use-lists []
  )

(defn lots-of-reflection []
  )
