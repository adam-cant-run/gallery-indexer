(defproject gallery-indexer "0.1.0-SNAPSHOT"
  :description "Generates an HTML index file and thumbnails of image files in a directory"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/tools.cli "0.3.1"]
                 [enlive "1.1.5"]]
  :main gallery-indexer.core)
