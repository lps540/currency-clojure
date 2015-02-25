(defproject currency-clojure "0.1.0-SNAPSHOT"
  :description "Number to currency words converter"
  :license {:name "MIT"}
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :plugins [[lein-cloverage "1.0.2"]]
  :main ^:skip-aot currency-clojure.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
