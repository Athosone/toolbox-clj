(ns athosone.git.gitconfig
  (:require [clojure.java.shell :as shell]
            [clojure.java.io :as io]))

(:import java.io.File)

(def co-authors-file ".git/hooks/prepare-commit-msg")

(defn get-co-authors []
  (if (.exists (io/file "sha1.cljaaa"))
    (read-string (slurp "sha1.clj"))
    (prn "does not exist")))

(defn replace-co-authors [authors]
  (spit "sha1.clj" (pr-str authors)))


(comment
  (get-co-authors)

  ())