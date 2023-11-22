(ns athosone.gitlab.pair
  (:require [clj-http.client :as client]
            [clojure.edn :as edn]))

(def base-url "https://gitlab.com/api/v4")
(def search-endpoint "/users?search=")

(def config (edn/read-string (slurp  "./config.edn")))
(def access-token (:GITLAB_PERSONAL_ACCESS_TOKEN config))

(defn search-user [user]
  (let [response (client/get (str base-url search-endpoint user)
                             {:headers {"Private-Token" access-token}
                              :as :json})]
    (map #(:username %1) (:body response))))

(comment
  (search-user "athosone")
  ())