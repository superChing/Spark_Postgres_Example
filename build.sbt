name := "SparkMovielens"

version := "1.0"

scalaVersion := "2.10.6"

mainClass in Compile := Some("compute.Run")

libraryDependencies ++= List(
  "com.typesafe.slick" %% "slick" % "3.1.0",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.1.0",
  "org.slf4j" % "slf4j-nop" % "1.7.10",
  "com.h2database" % "h2" % "1.4.187",
  "org.postgresql" % "postgresql" % "9.4.1208",
  "org.apache.spark" %% "spark-sql" % "1.6.0" % "provided",

  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)



//fork := true
// add a JVM option to use when forking a JVM for sbt 'run'
//javaOptions ++= Seq("-Xmx2G")