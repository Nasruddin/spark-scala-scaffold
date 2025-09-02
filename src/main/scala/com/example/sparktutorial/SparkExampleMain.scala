package com.example.sparktutorial

import com.example.sparktutorial.Analysis.transform

object SparkExampleMain extends App {
  
    val (inputPath, outputPath) = parseArgs(args = args)
    val data = sparkSession.read.option("header", "true").parquet(s"${inputPath}/users.parquet")
    //val data = sparkSession.read.option("header", "true").csv(s"${inputPath}/users.csv")
    private val analysisResult = transform(data = data)

    analysisResult.write.mode("overwrite").option("header", "true").parquet(outputPath)
}