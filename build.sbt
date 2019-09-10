name := "ResidentHomeAssignment"

version := "0.1"

scalaVersion := "2.12.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % "2.5.25",
  "com.typesafe.akka" %% "akka-http"   % "10.1.9",
  "com.lightbend.akka" %% "akka-stream-alpakka-kinesis" % "1.1.1",
  "com.lightbend.akka" %% "akka-stream-alpakka-mongodb" % "1.1.1"
)