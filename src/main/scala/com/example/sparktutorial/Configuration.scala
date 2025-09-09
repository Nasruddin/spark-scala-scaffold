package com.example.sparktutorial.config

import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

case class SparkSettings(
  appName: String,
  master: String,
  executorMemory: String,
  driverMemory: String,
  shufflePartitions: String
)

object Configuration {
  private val logger = LoggerFactory.getLogger(getClass)
  
  private val config = ConfigFactory.load()
  private val sparkConfig = config.getConfig("spark.settings")
  
  val sparkSettings: SparkSettings = try {
    val settings = SparkSettings(
      appName = sparkConfig.getString("appName"),
      master = sparkConfig.getString("master"),
      executorMemory = sparkConfig.getString("executorMemory"),
      driverMemory = sparkConfig.getString("driverMemory"),
      shufflePartitions = sparkConfig.getString("shufflePartitions")
    )
    logger.info(s"Loaded spark settings: $settings")
    settings
  } catch {
    case e: Exception =>
      logger.error(s"Failed to load configuration: ${e.getMessage}")
      // Provide default settings if config fails
      SparkSettings(
        appName = "SparkScalaScaffold",
        master = "local[*]",
        executorMemory = "2g",
        driverMemory = "2g",
        shufflePartitions = "200"
      )
  }
}
