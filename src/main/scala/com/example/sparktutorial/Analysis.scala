package com.example.sparktutorial

import org.apache.spark.sql.{DataFrame, functions => F}
import org.slf4j.LoggerFactory

object Analysis {
  private val logger = LoggerFactory.getLogger(getClass)

  def transform(data: DataFrame): DataFrame = {
    logger.info(s"Starting data transformation, record count: ${data.count()}")

    val result = data
      .withColumn("email_domain", F.split(F.col("email"), "@").getItem(1))
      .withColumn("processed_timestamp", F.current_timestamp())

    // Log some metrics
    val distinctDomains = result.select("email_domain").distinct().count()
    logger.info(s"Found ${distinctDomains} distinct email domains")

    result
  }
}