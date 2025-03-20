# Spark Scala Scaffold

## Overview

This repository provides a scaffold for Apache Spark applications written in Scala. It includes a structured project setup with essential configurations, making it easier to kickstart Spark-based data processing projects.

## Dependencies 
This setup is built using
- **Java 11**
- **Scala 2.13.16**
- **sbt 1.9.8**
- **Spark 3.5.4**

## Features

- Pre-configured **SBT** build system
- Sample Spark job implementation
- Converts parquet to CSV
- Organized project structure
- Logging setup using **SLF4J**
- Easy-to-run example with SparkSession


## Installation & Setup

Clone the repository and navigate to the project directory:

```sh
$ git clone https://github.com/Nasruddin/Spark-scala-scaffold.git
$ cd Spark-scala-scaffold
```

## Building the Project

Compile and package the application using SBT:

```sh
$ sbt clean compile
$ sbt package
```

## Running the Application

Run the application locally using SBT:

```sh
$ sbt run --input-path data --output-path output
```

Or submit the packaged JAR to a Spark cluster:

```sh
$ spark-submit --class com.example.Main \
  --master local[*] \
  target/scala-2.12/spark-scala-scaffold_2.12-0.1.jar \
  --input-path data --output-path output
```

## Project Structure

```
Spark-scala-scaffold/
├── src/
│   ├── main/
│   │   ├── scala/com/example/
│   │   │   ├── sparktutorial/
│   │   │   │   ├── Analysis.scala
│   │   │   │   ├── Configuration.scala
│   │   │   │   ├── package.scala
│   │   │   │   ├── SparkExampleMain.scala
│   ├── resources/
│   │   ├── application.conf
├── build.sbt
├── README.md
├── project/
├── target/
```

- `Analysis.scala` - Contains the main transformation logic for the Spark application.
- `Configuration.scala` - Handles the application configuration using `Typesafe Config`.
- `package.scala` - Contains utility functions and implicit values for the Spark session.
- `SparkExampleMain.scala` - Entry point for running Spark examples.
- `build.sbt` - Project dependencies and build configuration.
## Configuration

The configuration is managed using `Typesafe Config`. The configuration file `application.conf` should be placed in the `resources` directory.

### Example `application.conf`

```hocon
default {
  appName = "Spark Scala Basic Setup App"
  spark {
    settings {
      spark.master = "local[*]"
      spark.app.name = ${default.appName}
    }
  }
}
```

## Dependencies

Add the following dependencies to your `build.sbt` file:

```scala
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.5.4",
  "org.apache.spark" %% "spark-sql" % "3.5.4",
  "com.typesafe" % "config" % "1.4.1"
)
```

## Contributing

Contributions are welcome! Feel free to submit issues or pull requests.
