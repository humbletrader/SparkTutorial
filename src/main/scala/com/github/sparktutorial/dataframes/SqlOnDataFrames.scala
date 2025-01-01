package com.github.sparktutorial.dataframes

import com.github.sparktutorial.config.{SparkTutorialConfig, SparkTutorialConfigReader}
import com.github.sparktutorial.refdata.PersonWithAge
import com.github.sparktutorial.utils.logging.Logging
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{SaveMode, SparkSession}

object SqlOnDataFrames extends SparkTutorialConfigReader with Logging {

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

    log.info("selecting from DF when predicates are pushed down")
    personsFromDisk.createOrReplaceTempView("PersonsTable")
    val malesDataFrame = spark.sql("select * from PersonsTable where gender = 'M'")
    malesDataFrame.show()

    spark.stop
    log.info("spark tutorial run successfully !")
  }

}
