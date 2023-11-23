(ns athosone.git.gitconfig
  (:require
   [clojure.java.io :as io]))

(:import java.io.File)

(def co-authors-file ".git/hooks/prepare-commit-msg")

(defn get-co-authors []
  (when (.exists (io/file co-authors-file))
    (slurp co-authors-file)))


; Todo set file as executable if not already
(defn replace-co-authors [authors]
  (spit co-authors-file (reduce #(str %1 "Co-authored by: " %2 "\n") "" authors)))

(comment
  (get-co-authors)
  (replace-co-authors "toto")
  ())