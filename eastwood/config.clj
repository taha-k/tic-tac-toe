(disable-warning
  {:linter :constant-test
   :if-inside-macroexpansion-of #{'clojure.test/is}
   :within-depth 6
   :reason "delivery.generative-test/generative-test contains when invocation with constant test condition"})
