package com.github.sparktutorial.dataframes

import com.github.sparktutorial.config.SparkTutorialConfigReader
import com.github.sparktutorial.refdata.PersonWithAge
import com.github.sparktutorial.utils.logging.Logging
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{SaveMode, SparkSession}

object SqlOnDataFrames extends SparkTutorialConfigReader with Logging {

  def main(args: Array[String]): Unit = {

    implicit val spark: SparkSession = SparkSession.builder()
      .appName("DataFrameTutorial")
      .getOrCreate()

    implicit val config = readConfig(args)

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

    log.info("writing to parquet partitioned")
    personsDataFrame
      .write.mode(SaveMode.Overwrite)
      .partitionBy("gender")
      .parquet("/tmp/test/persons.parquet")

    log.info("reading from parquet")
    val personsFromDisk = spark.read.parquet("/tmp/test/persons.parquet")

    log.info("programmatic selecting columns and filtering")
    personsFromDisk.select("firstName", "lastName").filter("age > 18").show()

    log.info("selecting from DF when predicates are pushed down")
    personsFromDisk.createOrReplaceTempView("PersonsTable")
    val malesDataFrame = spark.sql("select * from PersonsTable where gender = 'M'")
    malesDataFrame.show()
    malesDataFrame.explain()
    //24/08/27 19:00:41 INFO DataSourceStrategy: Pruning directories with: isnotnull(gender#53),(gender#53 = M)
    //24/08/27 19:00:41 INFO FileSourceStrategy: Pushed Filters:
    //24/08/27 19:00:41 INFO FileSourceStrategy: Post-Scan Filters:
    //== Physical Plan ==
    //*(1) ColumnarToRow
    //+- FileScan parquet [
    //      id#49,firstName#50,lastName#51,age#52,gender#53]
    //      Batched: true,
    //      DataFilters: [],
    //      Format: Parquet,
    //      Location: InMemoryFileIndex(1 paths)[file:/tmp/test/persons.parquet],
    //      PartitionFilters: [isnotnull(gender#53), (gender#53 = M)],
    //      PushedFilters: [],
    //      ReadSchema: struct<id:int,firstName:string,lastName:string,age:int>

    log.info("deeper physical plan with pushed filters")
    spark.sql("select firstName from PersonsTable where age > 18").explain
    //24/08/27 19:06:28 INFO DataSourceStrategy: Pruning directories with:
    //24/08/27 19:06:28 INFO FileSourceStrategy: Pushed Filters: IsNotNull(age),GreaterThan(age,18)
    //24/08/27 19:06:28 INFO FileSourceStrategy: Post-Scan Filters: isnotnull(age#52),(age#52 > 18)
    //== Physical Plan ==
    //*(1) Project [firstName#50]
    //+- *(1) Filter (isnotnull(age#52) AND (age#52 > 18))
    //   +- *(1) ColumnarToRow
    //      +- FileScan parquet [firstName#50,age#52,gender#53]
    //          Batched: true,
    //          DataFilters: [isnotnull(age#52), (age#52 > 18)],
    //          Format: Parquet,
    //          Location: InMemoryFileIndex(1 paths)[file:/tmp/test/persons.parquet],
    //          PartitionFilters: [],
    //          PushedFilters: [IsNotNull(age), GreaterThan(age,18)],
    //          ReadSchema: struct<firstName:string,age:int>
    log.info("spark tutorial run successfully !")
  }

}
