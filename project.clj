(defproject om-tutorial "0.1.0-SNAPSHOT"
  :description "A Tutorial for Om 1.0.0 (next)"
  :dependencies [[org.clojure/clojure "1.8.0" :scope "provided"]
                 [org.clojure/clojurescript "1.9.494" :scope "provided"]
                 [devcards "0.2.1-6" :exclusions [org.omcljs/om]]
                 [datascript "0.13.3"]
                 [com.cognitect/transit-cljs "0.8.239"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [org.omcljs/om "1.0.0-alpha48"]
                 [figwheel-sidecar "0.5.7" :scope "test"]]

  :source-paths ["src"]

  :clean-targets ^{:protect false} ["resources/public/js"]

  :cljsbuild {
              :builds
              [
               {:id           "dev"
                :figwheel     true
                :source-paths ["src"]
                :compiler     {:main                 om-next-01.core
                               :asset-path           "js"
                               :output-to            "resources/public/js/main.js"
                               :output-dir           "resources/public/js"
                               :recompile-dependents true
                               :parallel-build       true
                               :verbose              false}}]}


  :profiles {
             :dev {:source-paths ["src/dev"]
                   :repl-options {:init-ns user
                                  :port    7001}}})

