package com.github.sparktutorial.datasets

import com.github.sparktutorial.config.SparkTutorialConfig
import com.github.sparktutorial.dataframes.SqlOnDataFrames.{log, readConfig}
import org.apache.spark.sql.SparkSession

class SqlOnDatasets {

  def main(args: Array[String]): Unit = {

    implicit val spark: SparkSession = SparkSession.builder()
      .appName("SqlOnDataFrames")
      .getOrCreate()

    implicit val config : SparkTutorialConfig = readConfig(args)

    log.info("reading from parquet")
    val personsFromDisk = spark.read.parquet("./output/write_parquet")
    //personsFromDisk.show()

    log.info("programmatic selecting columns and filtering")
    personsFromDisk
      .select("firstName", "lastName")
      .filter("age > 18")
      .show()


    log.info("programmatic aggregation")
    personsFromDisk
      .groupBy("gender")
      .avg("age")
      .show()

    spark.stop
    log.info("spark tutorial run successfully !")
  }

}
