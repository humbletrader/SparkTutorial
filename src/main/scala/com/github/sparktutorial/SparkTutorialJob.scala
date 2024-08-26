package com.trg.crimes

import com.github.sparktutorial.config.{SparkTutorialConfig, SparkTutorialConfigReader}
import com.github.sparktutorial.utils.logging.Logging
import org.apache.spark.sql.SparkSession

object SparkTutorialJob extends SparkTutorialConfigReader with Logging {

  def main(args: Array[String]): Unit = {

    implicit val spark : SparkSession = SparkSession.builder()
      .appName("SparkTutorial")
      .getOrCreate()
    implicit val config = readConfig(args)

    log.info(s"running spark tutorial with config $config")


    log.info("spark tutorial run successfully !")
  }
}
