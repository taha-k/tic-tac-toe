(defproject tic-tac-toe "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [environ "1.1.0"]
                 [compojure "1.6.1"]
                 [http-kit "2.3.0"]
                 [ring/ring-defaults "0.3.2"]
                 [metosin/ring-http-response "0.9.0"]
                 [org.clojure/data.json "0.2.6"]]
  :main ^:skip-aot tic-tac-toe.core
  :target-path "target/%s"
  :plugins [[lein-environ "1.1.0"]]
  :profiles {:uberjar {:aot :all}})
