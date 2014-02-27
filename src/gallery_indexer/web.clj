(ns gallery-indexer.web
  (:require [net.cgrand.enlive-html :as html]))

(html/defsnippet thumb "templates/thumbnail.html"
  [:.thumbnail]
  [thumb-map]
  [:a] (html/set-attr :href (:name thumb-map))
  [:img] (html/do->
          (html/set-attr :src (str ".thumbs/" (:name thumb-map)))
          (html/set-attr :alt (:name thumb-map))))

(html/deftemplate index-template "templates/index.html"
                  [thumbnail-list]
                  [:#title] (html/content "Gallery")
                  [:#thumbnails] (html/clone-for [t thumbnail-list]
                                                 (html/do-> (html/content (thumb t)))))


(defn gen-index
  [files]
  (apply str (index-template (for [f files]
                               {:name (.getName f)}))))

