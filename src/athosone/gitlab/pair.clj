(ns athosone.gitlab.pair
  (:require [clj-http.client :as client]
            [athosone.git.gitconfig :as gitconfig]
            [clojure.string :as str]
            [clojure.pprint :as pp]))


(def base-url nil)
(def access-token nil)

(def search-endpoint "/users?search=")
(defn set-base-url!
  [base-url]
  (alter-var-root (var base-url) (constantly base-url)))

(defn set-access-token!
  [access-token]
  (alter-var-root (var access-token) (constantly access-token)))

(defn search-user [user]
  (let [response (client/get (str base-url search-endpoint user)
                             {:headers {"Private-Token" access-token}
                              :as :json})]
    (map (fn [response] {:username (:username response) :name (:name response)}) (:body response))))

(defn- print-users [users]
  (let [indexed-map (zipmap (iterate inc 0) users)
        formatted-users (map (fn [[k v]] (hash-map :index k :user (:username v) :name (:name v))) indexed-map)]
    (pp/print-table formatted-users)))

(defn- select-user [users]
  (print-users users)
  (prn "Enter user number:")
  (let [user-idx (read-line)]
    (if (not (re-matches #"^\d+$" user-idx))
      (throw (ex-info "Invalid user number" {}))
      (let [selected-user (nth users (Integer/parseInt user-idx))]
        (prn (str "Selected user: " selected-user))
        (:username selected-user)))))

(defn refine-user [user]
  (let [potential-users (sort-by :name (vec (take 10 (search-user user))))]
    (when (empty? potential-users)
      (throw (ex-info (str "User " user " not found") {})))
    ; Ask for user selection
    (prn "Select user:")
    (select-user potential-users)))

(defn pair [{:keys [token url users]}]
  (when (empty? users)
    (throw (ex-info "No users provided" {})))
  (set-base-url! url)
  (set-access-token! token)
  (let [users (str/split users #",")]
    (gitconfig/replace-co-authors (vec (map #(refine-user %1) users)))))

(comment
  (def config (edn/read-string (slurp  "./config.edn")))
  (def base-url (get config :GITLAB_URL))
  (def access-token (get config :GITLAB_PERSONAL_ACCESS_TOKEN))
  (print-users [{:username "user" :name "toto"}, {:username "user2" :name "toto2"}])
  (sort-by :name (vec (search-user "dubois")))
  (map #(refine-user %1) ["Dubois"])
  (refine-user "Dubois")
  ())

; TODO check for login gitlab interactively or even forgerock