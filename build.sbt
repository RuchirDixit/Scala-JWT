
name := "Scala_jwtAuthentication"

version := "0.1"

scalaVersion := "2.12.2"
coverageEnabled := true
libraryDependencies ++= Seq("com.typesafe.akka" %% "akka-actor" % "2.5.23",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.23" % Test,
  "com.typesafe.akka" %% "akka-stream" % "2.5.23",
  "com.typesafe.akka" %% "akka-http" % "10.1.8",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.1.8" % Test,
  "com.typesafe.play" %% "play-json" % "2.6.7",
  "com.jason-goodwin" %% "authentikat-jwt" % "0.4.5",
  "de.heikoseeberger" %% "akka-http-play-json" % "1.20.0",
  "org.mongodb.scala" %% "mongo-scala-driver" % "4.1.1",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
  "ch.qos.logback" % "logback-classic" % "1.1.3" % Runtime,
  "com.github.daddykotex" %% "courier" % "3.0.0-M2",
  "com.nimbusds" % "nimbus-jose-jwt" % "9.3",
  "org.scalatest" %% "scalatest" % "3.2.2" % Test,
  "org.scalatestplus" %% "mockito-3-4" % "3.2.3.0" % "test"
)