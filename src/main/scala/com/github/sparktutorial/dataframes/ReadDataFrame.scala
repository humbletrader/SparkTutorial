package com.github.sparktutorial.dataframes

import com.github.sparktutorial.config.{SparkTutorialConfig, SparkTutorialConfigReader}
import com.github.sparktutorial.utils.logging.Logging
import org.apache.spark.sql.SparkSession

import java.io.File

/**
 * DEPENDS ON WriteDataFrame.scala
 */
object ReadDataFrame extends SparkTutorialConfigReader with Logging {

  def main(args: Array[String]): Unit = {

    val warehouseLocation = new File("spark-warehouse").getAbsolutePath //todo: add this to the config file

    implicit val spark: SparkSession = SparkSession.builder()
      .appName("ReadDataFrame")
      .config("spark.sql.warehouse.dir", warehouseLocation)
      .enableHiveSupport()
      .getOrCreate()

    implicit val config : SparkTutorialConfig = readConfig(args)

    log.info("reading from parquet")
    val personsFromDisk = spark.read.parquet("./output/write_parquet")
    personsFromDisk.show()

    log.info("reading from a table")
    val personsFromTable = spark.table("persons_table")
    personsFromTable.show

    spark.stop
    log.info("spark tutorial run successfully !")
  }

}
