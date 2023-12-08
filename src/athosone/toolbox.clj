(ns athosone.toolbox
  (:require [athosone.gitlab.pair :as gitlab]
            [cli-matic.core :refer [run-cmd]])
  (:gen-class))

;; https://github.com/l3nz/cli-matic
(def CONFIGURATION
  {:command "toolbox"
   :description "A collection of tools"
   :version "0.1.0"
   :subcommands [{:command "gitlab"
                  :description "Gitlab CLI"
                  :version "0.1.0"
                  :opts [{:as "Gitlab personal token, can also be set in env var GITLAB_ACCESS_TOKEN"
                          :default ""
                          :present true
                          :env "GITLAB_ACCESS_TOKEN"
                          :type :string
                          :option "token"
                          :short "t"}
                         {:as "Gitlab API URL, can also be set in env var GITLAB_URL, defaults to https://gitlab.michelin.com/api/v4"
                          :default "https://gitlab.michelin.com/api/v4"
                          :type :string
                          :env "GITLAB_URL"
                          :option "url"}]
                  :subcommands [{:command "pair"
                                 :description "Pair with a gitlab user, will add as Co-Author to commits"
                                 :opts [{:as "Gitlab users (comma separated)"
                                         :default ""
                                         :type :string
                                         :option "users"
                                         :short "u"}]
                                 :runs gitlab/pair}]}]})

;; TODO: improve config:
;; https://grishaev.me/en/clj-book-config/
(defn -main
  [& args]
  (run-cmd args CONFIGURATION))


(comment
  ;; (main gitlab pair ...)
  (-main "--help")
  (-main "gitlab" "pair" "-u" "aaa,bbb")
  ())
