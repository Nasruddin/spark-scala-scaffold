package com.example.sparktutorial

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions.{avg, col, concat_ws, count, lit, split, udf}

object Analysis {

  def transform(data: DataFrame): DataFrame = {
    val result = data

    printTotalRecords(result)
    showDistinctEmailDomains(result)
    printTotalResidence(result)

    val dfWithDistance = addDistanceColumn(result, 12.9716, 77.5946)
    dfWithDistance.show(truncate = false)

    result
  }

  private def printTotalRecords(data: DataFrame): Unit = {
    val records = data.count()
    println(s"Total records: $records")
  }

  private def showDistinctEmailDomains(data: DataFrame): Unit = {
    val emailDomains = data.select(split(col("email"), "@").getItem(1).as("domain")).distinct()
    emailDomains.show(truncate = false)
  }

  private def printTotalResidence(data: DataFrame): Unit = {
    val rr = data.groupBy("residence").count().orderBy(col("count").desc).count()
    println(s"Total residence: $rr")
  }

  private def addDistanceColumn(data: DataFrame, refLat: Double, refLon: Double): DataFrame = {
    val haversineUDF = udf((lat1: Double, lon1: Double, lat2: Double, lon2: Double) => {
      val R = 6371 // Earth's radius in km
      val dLat = Math.toRadians(lat2 - lat1)
      val dLon = Math.toRadians(lon2 - lon1)
      val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
              Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
              Math.sin(dLon / 2) * Math.sin(dLon / 2)
      val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
      R * c
    })

    data.withColumn("distance_km", haversineUDF(col("lat"), col("lon"), lit(refLat), lit(refLon)))
        .withColumn("full name", concat_ws(" ", col("firstName"), col("lastName")))
  }
}