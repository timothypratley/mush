(defproject mush "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]]
  :global-vars {*warn-on-reflection* true}
  :profiles {:dev {:dependencies [[criterium "0.4.3"]
                                  [clj-jgit "0.8.8"]
                                  [camel-snake-kebab "0.3.2"]]}}
  :test-selectors {:perf :perf
                   :default (complement :perf)}
  :jvm-opts ^:replace ["-server" "-Xms256m" "-Xmx256m" "-XX:+AggressiveOpts" "-XX:+UseCompressedOops"])
