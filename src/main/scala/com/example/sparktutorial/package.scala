package com.example

import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkConf
import com.example.sparktutorial.config.Configuration

// To run ---> sbt "run --input-path data --output-path output"

package object sparktutorial {

  private val appName: String = Configuration.appName

  private lazy val sparkConf: SparkConf = {
    val coreConf = new SparkConf().setAppName(appName)
    Configuration.Spark.settings.foreach { case (key, value) =>
      coreConf.setIfMissing(key, value)
    }
    coreConf
  }

  lazy implicit val sparkSession: SparkSession = {
    SparkSession.builder().config(sparkConf).getOrCreate()
  }

  def parseArgs(args: Array[String]): (String, String) = {
    val parsedArgs = args.grouped(2).collect {
      case Array(key, value) => key -> value
    }.toMap

    val inputPath = parsedArgs.getOrElse("--input-path", throw new IllegalArgumentException("Input path not provided"))
    val outputPath = parsedArgs.getOrElse("--output-path", throw new IllegalArgumentException("Output path not provided"))

    (inputPath, outputPath)
  }
}
