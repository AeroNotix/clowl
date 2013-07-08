(ns clowl.clowl
  (:use compojure.core)
  (:use clowl.connection)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [clojure.java.jdbc :as j]
            [clojure.java.jdbc.sql :as s]
            [clabango.parser :refer [render]]
            [clojure.java.io :as io]))

(defn get-blog [blogname]
  (j/query mysql-db
           (s/select * :blog_entry (s/where {:blog_url blogname}))))

(defn get-template [template]
  (-> template io/resource io/file))

(defn get-blog-list []
  (j/query mysql-db
           (s/select * :blog_entry)))

(defn blog [blogname]
  (let [tmpl (get-template "templates/blog.html")
        b (get-blog blogname)]
    (render tmpl {:blog_title (:blog_title (first b))
                  :blog_post (:blog_post (first b))
                  :STATIC_URL "/static/"})))

(defn bloglist []
  (let [tmpl (get-template "templates/blogmode.html")]
    (render tmpl {:blog (get-blog-list)
                  :STATIC_URL "/static/"})))

(defroutes app-routes
  (GET "/blog/:blogname" [blogname]
       (blog blogname))
  (GET "/blog/" []
       (bloglist))
  (GET "/" []
       (let [tmpl (get-template "templates/base.html")]
         (render tmpl {:STATIC_URL "/static/"})))
  (route/resources "/static/")
  (GET "/about/" []
       (blog "about"))
  (route/not-found
   (let [tmpl (get-template "templates/blog.html")]
     (render tmpl {:blog_title "404"
                   :blog_post "Not found"
                   :STATIC_URL "/static/"}))))

(def app (handler/site app-routes))