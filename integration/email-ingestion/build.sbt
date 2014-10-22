name := "EmailIngestion"

version := "0.9-SNAPSHOT"

scalaVersion := "2.11.2"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "com.kenshoo" %% "metrics-play" % "2.3.0_0.1.6",
  "org.springframework" % "spring-context" % "3.2.11.RELEASE",
  "org.springframework" % "spring-expression" % "3.2.11.RELEASE",
  "org.springframework" % "spring-aop" % "3.2.11.RELEASE",
  "org.springframework" % "spring-test" % "3.2.11.RELEASE" % "test",
  "org.mongodb" % "mongo-java-driver" % "2.12.2",
  "org.jongo" % "jongo" % "1.1",
  "uk.co.panaxiom" %% "play-jongo" % "0.7.1-jongo1.0",
  "io.fastjson" % "boon" % "0.28"
)


lazy val root = (project in file("."))
.aggregate(common, api, client)
.dependsOn(common, api, client)
.enablePlugins(PlayJava)

lazy val common = project.in(file("modules/common")).enablePlugins(PlayJava)

lazy val api = project.in(file("modules/api")).dependsOn(common).enablePlugins(PlayJava)

lazy val client = project.in(file("modules/client"))

// The Typesafe repository
resolvers ++= Seq(
"Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
"sonatype-snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
"sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases")

resolvers += Resolver.mavenLocal