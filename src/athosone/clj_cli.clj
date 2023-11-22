(ns athosone.clj-cli
  (:require [athosone.password :refer [generate-password]])
  (:gen-class))

(defn greet
  "Callable entry point to the application."
  [data]
  (println (str "Hello, " (or (:name data) "World") "!")))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (generate-password 10))

(-main)
