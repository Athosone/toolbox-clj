(ns athosone.git.gitconfig
  (:require
   [clojure.java.io :as io]
   [clojure.string :as s]))

(:import java.io.File)

(def co-authors-file ".git/co-authors.txt")
(def prepare-commit-msg-file ".git/hooks/prepare-commit-msg")
(def resource-preparemsg-script "prepare-commit-msg.sh")

(defn get-co-authors []
  (when (.exists (io/file co-authors-file))
    (slurp co-authors-file)))

(defn- format-authors [authors]
  (reduce #(str %1 "Co-authored by: " %2 "\n") "" authors))

(defn- generate-co-authors [authors]
  (spit co-authors-file
        (cond
          (vector? authors) (format-authors authors)
          (sequential? authors) (format-authors (vec authors))
          (string? authors) (format-authors (s/split authors #",")))))

(defn- generate-prepare-commit-hook []
  (spit prepare-commit-msg-file (slurp (io/resource resource-preparemsg-script)))
  (when (not (.canExecute (io/file prepare-commit-msg-file)))
    (.setExecutable (io/file prepare-commit-msg-file) true)))

(defn replace-co-authors [authors]
  (prn (str "Replacing co-authors with " (s/join "," authors)))
  (generate-prepare-commit-hook)
  (generate-co-authors authors))

(comment
  (get-co-authors)
  (replace-co-authors ["athosone"])
  (slurp co-authors-file)
  (generate-co-authors "")
  (slurp (io/resource resource-preparemsg-script))
  ())