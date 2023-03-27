val scala3Version = "3.2.2"

lazy val root = project
  .in(file("."))
  .settings(
    name := "coinTradingSystem",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test,
    libraryDependencies += "org.knowm.xchange" % "xchange-core" % "5.1.0",
    libraryDependencies += "org.knowm.xchange" % "xchange-binance" % "5.1.0",
    libraryDependencies += "org.knowm.xchange" % "xchange-kucoin" % "5.1.0",
    libraryDependencies += "org.knowm.xchange" % "xchange-huobi" % "5.1.0",
    libraryDependencies += "org.knowm.xchange" % "xchange-gateio" % "5.1.0",
    libraryDependencies += "org.awaitility" % "awaitility" % "4.2.0",
    libraryDependencies += "org.slf4j" % "slf4j-nop" % "2.0.7",
    libraryDependencies += "com.typesafe.slick" % "slick_2.13" % "3.5.0-M2",
    libraryDependencies += "org.xerial" % "sqlite-jdbc" % "3.39.2.1"
  )
