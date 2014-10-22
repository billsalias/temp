name := "SubProject-common"

version := "0.9-SNAPSHOT"

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
 "com.codahale.metrics" % "metrics-core" % "3.0.1",
 "com.codahale.metrics" % "metrics-json" % "3.0.1",
 "com.codahale.metrics" % "metrics-jvm" % "3.0.1",
 "com.codahale.metrics" % "metrics-logback" % "3.0.1",
 "com.kenshoo" %% "metrics-play" % "2.3.0_0.1.6",
 "org.mongodb" % "mongo-java-driver" % "2.12.2",
 "org.jongo" % "jongo" % "1.1",
 "uk.co.panaxiom" %% "play-jongo" % "0.7.1-jongo1.0"
)


