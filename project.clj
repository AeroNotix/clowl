(defproject myproject "0.1"
  :description "A small blogging engine"
  :license "BSD"
  :url "https://github.com/AeroNotix/clowl"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.5"]
                 [clabango "0.5"]
                 [org.clojure/java.jdbc "0.3.0-alpha4"]
                 [mysql/mysql-connector-java "5.1.25"]]
  :ring {:handler clowl.clowl/app}
  :plugins [[lein-ring "0.8.5"]])