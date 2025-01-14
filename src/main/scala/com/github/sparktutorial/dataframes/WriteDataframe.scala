package com.github.sparktutorial.dataframes

import com.github.sparktutorial.config.{SparkTutorialConfig, SparkTutorialConfigReader}
import com.github.sparktutorial.refdata.PersonWithAge
import com.github.sparktutorial.utils.logging.Logging
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{SaveMode, SparkSession}

import java.io.File

object WriteDataframe extends SparkTutorialConfigReader with Logging {

  def main(args: Array[String]): Unit = {

    val warehouseLocation = new File("spark-warehouse").getAbsolutePath

    implicit val spark: SparkSession = SparkSession.builder()
      .appName("WriteDataFrame")
      .config("spark.sql.warehouse.dir", warehouseLocation)
      .enableHiveSupport()
      .getOrCreate()

    implicit val config : SparkTutorialConfig = readConfig(args)

    log.info(s"running spark tutorial with config $config")
    val personSeq = Seq.tabulate(100){idx =>
      PersonWithAge(
        idx,
        s"fn $idx",
        s"ln $idx",
        idx % 100,
        if(idx % 2 == 0) "M" else "F"
      )
    }

    log.info("creating a dataframe from a RDD")
    val personsRdd : RDD[PersonWithAge]= spark.sparkContext.parallelize(personSeq)
    import spark.implicits._
    val personsDataFrame = personsRdd.toDF()
    personsDataFrame.show()

    log.info("writing to parquet partitioned on disk by gender and on memory by ")
    personsDataFrame
      .write
      .mode(SaveMode.Overwrite)
      .partitionBy("gender")
      .parquet("./output/write_parquet/")

    log.info("writing to table partitioned")
    personsDataFrame
      .write
      .mode(SaveMode.Overwrite)
      .partitionBy("gender")
      .bucketBy(5, "id")
      .saveAsTable("persons_table")

    val personsFromTable = spark.read.table("persons_table")
    personsFromTable.show

    val descSQL = spark.sql(s"DESC EXTENDED persons_table")
      //.filter($"col_name".contains("Partition") || $"col_name".contains("Bucket") || $"col_name".contains("Sort"))
    descSQL.show(numRows = 100, truncate = false)

    spark.stop()
  }

}
