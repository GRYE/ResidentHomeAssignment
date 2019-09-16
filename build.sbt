name := "ResidentHomeAssignment"

version := "0.1"

scalaVersion := "2.12.0"

libraryDependencies ++= Seq(
  "org.mongodb.scala" %% "mongo-scala-driver" % "2.6.0",
  "org.mongodb.scala" %% "mongo-scala-bson" % "2.7.0",
  "com.typesafe.akka" %% "akka-stream" % "2.5.25",
  "com.typesafe.akka" %% "akka-http"   % "10.1.9",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.9"
)