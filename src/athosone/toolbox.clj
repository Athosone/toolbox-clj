(ns athosone.toolbox
  (:require [athosone.gitlab.pair :as gitlab]
            [cli-matic.core :refer [run-cmd]])
  (:gen-class))

(def CONFIGURATION
  {:command "gitlab"
   :description "Gitlab CLI"
   :version "0.1.0"
   :opts [{:as "Gitlab personal token"
           :default ""
           :type :string
           :option "token"
           :short "t"}]
   :subcommands [{:command "pair"
                  :description "Pair with a gitlab user, will add as Co-Author to commits"
                  :opts [{:as "Gitlab users (comma separated)"
                          :default ""
                          :type :string
                          :option "users"
                          :short "u"}]
                  :runs gitlab/pair}]})


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!")
  (run-cmd args CONFIGURATION))


(comment
  ;; (main gitlab pair ...)
  ;; (main gitlab pr ...)
  (-main "gitlab" "pair" "-u" "athosone,athosone3"))