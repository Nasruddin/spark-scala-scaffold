package com.example.sparktutorial

import org.slf4j.LoggerFactory
import org.apache.spark.sql.functions._

object SparkExampleMain extends App {
  private val logger = LoggerFactory.getLogger(getClass)
  
  try {
    implicit val spark = sparkSession
    import spark.implicits._
    
    parseArgs(args) match {
      case Right(AppArgs(inputPath, outputPath)) =>
        // Create sample data if input path doesn't exist
        if (!new java.io.File(inputPath).exists()) {
          logger.info(s"Creating sample data at $inputPath")
          
          // Create sample DataFrame
          val sampleData = Seq(
            (1, "john.doe@example.com", "John", "Doe", 12.9716, 77.5946),
            (2, "jane.smith@example.com", "Jane", "Smith", 13.0827, 77.5877)
          ).toDF("id", "email", "firstName", "lastName", "latitude", "longitude")
          
          // Save sample data
          sampleData.write
            .mode("overwrite")
            .parquet(s"$inputPath/users.parquet")
        }
        
        logger.info(s"Reading data from: $inputPath")
        val inputData = spark.read
          .option("header", "true")
          .parquet(s"$inputPath/users.parquet")
        
        val transformedData = Analysis.transform(inputData)
        
        logger.info(s"Writing results to: $outputPath")
        transformedData.write
          .mode("overwrite")
          .option("header", "true")
          .parquet(outputPath)
        
        logger.info("Processing completed successfully")
      
      case Left(error) =>
        logger.error(s"Invalid arguments: ${error.message}")
        System.exit(1)
    }
  } catch {
    case e: Exception =>
      logger.error("Application failed", e)
      System.exit(1)
  } finally {
    sparkSession.stop()
  }
}