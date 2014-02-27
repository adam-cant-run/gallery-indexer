(ns gallery-indexer.core
  (:use gallery-indexer.web)
  (:use clojure.tools.cli)
  (:gen-class))

;; These are the image file types that are understood.
(def img-file-extensions [".jpg" ".jpeg" ".png" ".gif"])

(defn calc-thumb-dimensions
  "For an image, return a vec containing the width and height
  of a thumbnail, where the maximum of either value is 128 pixels."
  [src-img]
  (let [w (.getWidth src-img)
        h (.getHeight src-img)
        max-dim (if (> h w) h w)
        scale (/ max-dim 128)]
    [(int (/ w scale)) (int (/ h scale))]))

(defn gen-thumbnail
  "Generate a thumbnail of an image file"
  [img-file]
  (let [thumb-dir (clojure.java.io/file (.getParentFile img-file) ".thumbs")
        thumb-file (clojure.java.io/file thumb-dir (.getName img-file))]

    (when (not (.exists thumb-dir))
      (.mkdir thumb-dir))

    (let [src-img (javax.imageio.ImageIO/read img-file)
          [width height] (calc-thumb-dimensions src-img)
          resized-img (java.awt.image.BufferedImage. width height java.awt.image.BufferedImage/TYPE_INT_RGB)
          gfx-2d (.createGraphics resized-img)]
         (.drawImage gfx-2d src-img 0 0 width height nil)
         (.dispose gfx-2d)
         (javax.imageio.ImageIO/write resized-img "jpg" thumb-file))))

(defn is-image-file
  "Is the supplied file an image file?"
  [file]
  (let [filename (.toLowerCase (.getName file))
        idx (.indexOf filename ".")]
    (when (and (.isFile file)
               (>= idx 0))
      (let [extension (.substring filename idx)]
        (some #(= extension %) img-file-extensions)))))

(defn list-image-files
  "Create a list of image files in the named directory"
  [directory]
  (sort-by #(.getName %)
           (for [f (seq (.listFiles (clojure.java.io/file directory)))
             :when (and (is-image-file f)
                   (not (= ".thumbs" (.getName f))))]
             f)))

(def options-defn
  [["-d" "--directory DIR" "Directory containing image files"
    :default "."]])

(defn -main
  "Generates an index page of thumbnails"
  [& args]
  (let [parsed-options (parse-opts args options-defn)
        directory (:directory (:options parsed-options))]
    (let [img-files (list-image-files directory)]
      (doseq [img img-files] ;; Generate thumbnails, non-lazy
        (gen-thumbnail img))
      (spit (str directory "/index.html") (gen-index img-files)))))


