# tic-tac-toe

A Clojure based API for playing Tic-Tac-Toe

## Installation

You need Leiningen to run this project

### development:

Setting up REPL in IntelliJ IDEA / Cursive:
* Go to Run -> Edit Configurations...
* Add new configuration -> Clojure REPL -> Local
* Select "Run nREPL with Leiningen"
* Save configuration

Run server in REPL: Open REPL, run `(start-server)` or `(start-server 1234)`
  to use a custom port (default is **8089**).
  Stop server by running `(stop-server)`.
  Code changes are automatically reloaded when a file is saved.

Using an IDE is recommended for development work. For trivial one-off
work, the server can be compiled and started also from command line:
`PORT=8089 lein do clean, run`.

Swagger Api doc: `http://localhost:8089/api-docs` or `http://localhost:<your-custom-port>/api-docs`

Run application: `lein run`
Run all tests: `lein test`

Run all tests, watch for changing files: `lein test-refresh`

Test coverage: `lein cloverage`

Linting: `lein eastwood`
Note: macros in clojure.test often produce false errors about constant test expressions.

## License

Copyright © 2019 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
