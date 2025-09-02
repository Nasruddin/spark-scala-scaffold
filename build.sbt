scalaVersion := "2.13.16"

name := "sparktutorial"
organization := "com.example"
version := "1.0"

libraryDependencies ++= Seq(
	"org.apache.spark" %% "spark-core" % "4.0.0" % Provided,
	"org.apache.spark" %% "spark-sql" % "4.0.0" % Provided,
	"com.typesafe" % "config" % "1.4.4",
)

/*
* To create fat jar
* */
assembly / mainClass := Some("com.example.sparktutorial.SparkExampleMain")
assembly / assemblyMergeStrategy := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}


fork := true