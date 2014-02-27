# gallery-indexer

A simple Clojure program to generate thumbnails and a static index.html page with links to the images.

This code may contain bugs and is unlikely to be idiomatic Clojure (This project is part of my learning process).

## Usage

Compile with e.g.

```bash
lein uberjar
```

And run with e.g.

```bash
java -jar target/gallery-indexer-0.1.0-SNAPSHOT-standalone.jar --directory ~/Pictures
```

## License

Copyright © 2014 Adam Turnbull

Distributed under the Eclipse Public License, the same as Clojure.
