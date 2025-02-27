package com.example.sparktutorial.config

import com.typesafe.config.ConfigFactory
import scala.collection.JavaConverters._

object Configuration {
    /* The config location can be overridden with -Dconfig.file=path/to/config-file */
    private lazy val defaultConfig = ConfigFactory.load("application.conf")
    private val config = ConfigFactory.load().withFallback(defaultConfig)

    config.checkValid(ConfigFactory.defaultReference(), "default")

    private lazy val appConfig = config.getConfig("default")
    lazy val appName: String = appConfig.getString("appName")

    object Spark {
        private val spark = appConfig.getConfig("spark")
        private val _settings = spark.getObject("settings")
        
        lazy val settings: Map[String, String] = _settings.unwrapped().asScala.map {case (k, v) => k -> v.toString}.toMap
        print(s"settings: ${settings}")
    }
}
