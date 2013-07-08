(ns clowl.clowl
  (:use compojure.core)
  (:use clowl.connection)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [clojure.java.jdbc :as j]
            [clojure.java.jdbc.sql :as s]))

(defn get-blog [blogname]
  (j/query mysql-db
           (s/select * :blog_entry (s/where {:blog_url blogname}))))

(defn get-blog-list []
  (j/query mysql-db
           (s/select * :blog_entry)))

(defn blog [blogname]
  (let [tmpl (-> "templates/blog.html" io/resource io/file)
        b (get-blog blogname)]
    (render tmpl {:blog_title (:blog_title b)
                  :blog_post (:blog_post b)
                  :STATIC_URL "/static/"})))

(defn bloglist []
  (let [tmpl (-> "templates/blogmode.html" io/resource io/file)]
    (render tmpl {:blog (get-blog-list)
                  :STATIC_URL "/static/"})))

(defroutes app-routes
  (GET "/blog/:blogname" [blogname]
       (blog blogname))
  (GET "/blog/" []
       (bloglist))
  (GET "/" []
       (blog "base"))
  (GET "/about" []
       (blog "about"))
  (route/not-found "<h1>Page not found</h1>"))

(def app
     (handler/site app-routes))