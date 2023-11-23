(ns athosone.gitlab.pair
  (:require [clj-http.client :as client]
            [clojure.edn :as edn]
            [athosone.git.gitconfig :as gitconfig]
            [clojure.string :as str]))

(def base-url "https://gitlab.com/api/v4")
(def search-endpoint "/users?search=")

(def config (edn/read-string (slurp  "./config.edn")))
(def access-token (:GITLAB_PERSONAL_ACCESS_TOKEN config))

(defn search-user [user]
  (let [response (client/get (str base-url search-endpoint user)
                             {:headers {"Private-Token" access-token}
                              :as :json})]
    (map #(:username %1) (:body response))))

(defn refine-user [user]
  (let [potential-users (search-user user)]
    (when (empty? potential-users)
      (throw (ex-info (str "User " user " not found") {})))
    ; Ask for user selection

    ))

(defn pair [comma-sep-users]
  (when (empty? comma-sep-users)
    (throw (ex-info "No users provided" {})))
  (let [users (str/split comma-sep-users #",")] 
    (gitconfig/replace-co-authors (map #(search-user %1) users))))

(comment
  (search-user "athosone")
  (pair "")
  (pair "athoso")
  (str/split "athosone,athosone2" #",")
  ())

; TODO check for login gitlab interactively or even forgerock