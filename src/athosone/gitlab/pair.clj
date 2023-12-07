(ns athosone.gitlab.pair
  (:require [clj-http.client :as client]
            [clojure.edn :as edn]
            [athosone.git.gitconfig :as gitconfig]
            [clojure.string :as str]
            [clojure.pprint :as pp]))

;; (def config (edn/read-string (slurp  "./config.edn")))

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
    (map #(:username %1) (:body response))))

(comment
  (print-users ["ccc", "aaaa", "bbbb"])
  (pp/print-table (reduce into {} (map-indexed #(assoc {} %1 %2) ["aaaa", "bbbb"])))
  (pp/print-table  (zipmap (iterate inc 1) ["aaaa", "bbbb"]))
  (def y (zipmap (iterate inc 1) ["aaaa", "bbbb"]))
  y
  (def x (vec (map (fn [[k v]] (hash-map k v)) y)))
  x
  (def z (map (fn [[k v]] (hash-map :index k :user v)) y))  ;; (hash-map :index "1" :user user)) ["aaaa", "bbbb"]))
  (pp/print-table (sort-by :user z))
  ())
(defn- print-users [users]
  ;; (let [formatted-users (map-indexed (fn [idx user] ({:index (str idx) :username user})) users)]
  ;; Create a map with index and username
  (let [indexed-map (zipmap (iterate inc 0) users)
        formatted-users (map (fn [[k v]] (hash-map :index k :user v)) indexed-map)]
    (pp/print-table formatted-users)))

(defn- select-user [users]
  (print-users users)
  (prn "Enter user number:")
  (let [user-idx (read-line)]
    (if (not (re-matches #"^\d+$" user-idx))
      (throw (ex-info "Invalid user number" {}))
      (let [selected-user (nth users (Integer/parseInt user-idx))]
        (prn (str "Selected user: " selected-user))
        selected-user))))

(defn refine-user [user]
  (let [potential-users (sort (vec (take 5 (search-user user))))]
    (when (empty? potential-users)
      (throw (ex-info (str "User " user " not found") {})))
    ; Ask for user selection
    (prn "Select user:")
    (select-user potential-users)))

;; TODO token and url globally
(defn pair [{:keys [token url users]}]
  (when (empty? users)
    (throw (ex-info "No users provided" {})))
  (set-base-url! url)
  (set-access-token! token)
  (let [users (str/split users #",")]
    (gitconfig/replace-co-authors (vec (map #(refine-user %1) users)))))

(comment
  (print-users ["aaa" "bbb"])
  (select-user ["aaa" "bbb"])
  (search-user "ayrton")
  (pair "")
  (pair "ayrton werck,michel")
  (pair "werck")
  (str/split "athosone,athosone2" #",")
  (str/split "athosone2" #",")
  (refine-user "athos,a")

  ())

; TODO check for login gitlab interactively or even forgerock