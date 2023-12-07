(ns athosone.git.gitconfig
  (:require
   [clojure.java.io :as io]
   [clojure.string :as s]))

(:import java.io.File)

(def co-authors-file ".git/hooks/prepare-commit-msg")


; Todo set file as executable if not already
(defn get-co-authors []
  (when (.exists (io/file co-authors-file))
    (slurp co-authors-file)))

(defn- format-authors [authors]
  (reduce #(str %1 "Co-authored by: " %2 "\n") "" authors))

(defn- generate-co-authors [authors]
  (cond
    (vector? authors) (format-authors authors)
    (sequential? authors) (format-authors (vec authors))
    (string? authors) (format-authors (s/split authors #","))))

(defn replace-co-authors [authors]
  (prn (str "Replacing co-authors with " (s/join "," authors)))
  (spit co-authors-file (generate-co-authors authors))
  (when (not (.canExecute (io/file co-authors-file)))
    (.setExecutable (io/file co-authors-file) true)))

(comment
  (get-co-authors)
  (replace-co-authors ["aaa", "toaaao"])
  (slurp co-authors-file)
  (generate-co-authors "")
  ())