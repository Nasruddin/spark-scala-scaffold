scalaVersion := "2.13.16"

name := "sparktutorial"
organization := "com.example"
version := "1.0"

libraryDependencies ++= Seq(
	"org.apache.spark" %% "spark-core" % "3.5.4",
	"org.apache.spark" %% "spark-sql" % "3.5.4",
	"com.typesafe" % "config" % "1.3.1",
)


fork := true