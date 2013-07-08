(ns clowl.clowl
  (:use compojure.core)
  (:use clowl.connection)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [clojure.java.jdbc :as j]
            [clojure.java.jdbc.sql :as s]))

(defn get-blog [blogname]
  (j/query mysql-db
           (s/select :blog_post :blog_entry (s/where {:blog_url blogname}))))

(defn blog [blogname]
  (do
    (println (type (seq (get-blog blogname))))
    (seq (get-blog blogname))))

(defroutes app-routes
  (GET "/blog/:blogname" [blogname]
       (blog blogname))
  (GET "/" []
       (blog "base"))
  (GET "/about" []
       (blog "about"))
  (route/not-found "<h1>Page not found</h1>"))

(def app
     (handler/site app-routes))