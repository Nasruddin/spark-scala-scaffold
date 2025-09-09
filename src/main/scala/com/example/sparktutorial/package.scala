package com.example

import org.apache.spark.sql.SparkSession
import com.example.sparktutorial.common.{AppError, ValidationError}
import com.example.sparktutorial.config.Configuration
import org.slf4j.LoggerFactory
import com.example.sparktutorial.common.ErrorHandling.withErrorHandling

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.hadoop.shaded.org.checkerframework.checker.units.qual.s

// To run ---> sbt "run --input-path data --output-path output"

  case class Metrics(
    totalRecords: Long,
    distinctDomains: Long,
    totalResidences: Long
  )
package object sparktutorial {

  private val logger = LoggerFactory.getLogger(getClass)
  
  case class AppArgs(inputPath: String, outputPath: String)

  lazy val sparkSession: SparkSession = {
    val settings = Configuration.sparkSettings
    SparkSession
      .builder()
      .appName(settings.appName)
      .master(settings.master)
      .config("spark.executor.memory", settings.executorMemory)
      .config("spark.driver.memory", settings.driverMemory)
      .config("spark.sql.shuffle.partitions", settings.shufflePartitions)
      .getOrCreate()
  }

  def parseArgs(args: Array[String]): Either[AppError, AppArgs] = {
     if (args.length < 4) {
      Left(ValidationError(
        """Please provide both input and output paths:
          |sbt "run --input-path data --output-path output"""".stripMargin))
    } else {
      try {
        val inputPath = args(1)  // Value after --input-path
        val outputPath = args(3) // Value after --output-path
        Right(AppArgs(inputPath, outputPath))
      } catch {
        case e: Exception =>
          Left(ValidationError("Error parsing arguments. Use: --input-path data --output-path output"))
      }
    }
  }

  def transform(data: DataFrame): Either[AppError, DataFrame] = {
    withErrorHandling {
      logger.info(s"Starting data transformation with ${data.count()} records")
      val metrics = calculateMetrics(data)
      logMetrics(metrics)

      data.transform(addDistanceColumn(_, 12.9716, 77.5946))
        .transform(enrichData)
        .cache()
    }("Error during data transformation")
  }

  private def calculateMetrics(df: DataFrame): Metrics = {
    val totalRecords = df.count()
    val distinctDomains = df.select(split(col("email"), "@").getItem(1)).distinct().count()
    val totalResidences = df.groupBy("residence").count().count()

    Metrics(totalRecords, distinctDomains, totalResidences)
  }

  private def logMetrics(metrics: Metrics): Unit = {
    logger.info(s"""
    |Data Processing Metrics:
    |Total Records: ${metrics.totalRecords}
    |Distinct Email Domains: ${metrics.distinctDomains}
    |Total Residences: ${metrics.totalResidences}
    """.stripMargin)
  }

  private def addDistanceColumn(df: DataFrame, lat: Double, long: Double): DataFrame = {
    df.withColumn("distance_km", 
      sqrt(pow(col("latitude") - lit(lat), 2) + pow(col("longitude") - lit(long), 2)) * 111.2)
  }

  private def enrichData(df: DataFrame): DataFrame = {
    df.withColumn("full_name", concat_ws(" ", col("firstName"), col("lastName")))
      .withColumn("processed_dt", current_timestamp())
  }

  // Cleanup resources
  def cleanup(): Unit = {
    if (sparkSession != null) {
      sparkSession.stop()
    }
  }
}
