# scalatra-react-all #

A light weight isomorphic web application using Scalatra as a back-end freamework and React as a front-end framework. Nashorn is used for server rendering.

## Prerequisite ##
1. Java >= 1.8.0_144
2. NodeJs >= 6.11.3
3. sbt >= 1.0.2

## Build & Run ##

```sh
$ cd scalatra-react-all
$ ./sbt
> jetty:start
> browse
```

If `browse` doesn't launch your browser, manually open [http://localhost:8080/](http://localhost:8080/) in your browser.

## Packaging ##

```sh
$ cd scalatra-react-all
$ ./sbt
> pack
> exit

$ cd ./target/output
$ java -jar scalatra-react-all_2.12-0.1.0-SNAPSHOT.jar
```

Embedded Jetty is used to create executable jar.

`pack` creates and copies all dependencies to **./target/output**.
If you don't like the output structure, please consider [sbt-assembly](https://github.com/sbt/sbt-assembly) for fat Jar or [sbt-pack](https://github.com/xerial/sbt-pack) for distributable Scala packages that include dependent jars and launch scripts.

## Bugs and Issues ##
Have a bug or an issue with this template? [Open a new issue](https://github.com/nudemeth/scalatra-react-isomorphic/issues)

## License ##
Code released under the [Unlicense](https://github.com/nudemeth/scalatra-react-isomorphic/blob/master/LICENSE) license
