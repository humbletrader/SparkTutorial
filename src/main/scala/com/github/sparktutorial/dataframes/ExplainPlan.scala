package com.github.sparktutorial.dataframes

import com.github.sparktutorial.config.{SparkTutorialConfig, SparkTutorialConfigReader}
import com.github.sparktutorial.utils.logging.Logging
import org.apache.spark.sql.SparkSession

import java.io.File

object ExplainPlan extends SparkTutorialConfigReader with Logging {

  def main(args: Array[String]): Unit = {

    val warehouseLocation = new File("spark-warehouse").getAbsolutePath //todo: read this from config

    implicit val spark: SparkSession = SparkSession.builder()
      .appName("DataFrameEplainPlan")
      .config("spark.sql.warehouse.dir", warehouseLocation)
      .enableHiveSupport()
      .getOrCreate()

    implicit val config : SparkTutorialConfig = readConfig(args)

    log.info("selecting from DF")
    val malesDataFrame = spark.sql("select * from persons_table where gender = 'M'")
    malesDataFrame.show()
    malesDataFrame.explain(extended = true)
//    25/01/01 17:34:07 INFO DataSourceStrategy: Pruning directories with: isnotnull(gender#4),(gender#4 = M)
//    25/01/01 17:34:07 INFO FileSourceStrategy: Pruned 0 out of 5 buckets.
//    25/01/01 17:34:07 INFO FileSourceStrategy: Pushed Filters:
//    25/01/01 17:34:07 INFO FileSourceStrategy: Post-Scan Filters:
//    == Parsed Logical Plan ==
//    'Project [*]
//    +- 'Filter ('gender = M)
//    +- 'UnresolvedRelation [persons_table], [], false
//
//    == Analyzed Logical Plan ==
//    id: int, firstName: string, lastName: string, age: int, gender: string
//    Project [id#0, firstName#1, lastName#2, age#3, gender#4]
//    +- Filter (gender#4 = M)
//    +- SubqueryAlias spark_catalog.default.persons_table
//    +- Relation spark_catalog.default.persons_table[id#0,firstName#1,lastName#2,age#3,gender#4] parquet
//
//    == Optimized Logical Plan ==
//    Filter (isnotnull(gender#4) AND (gender#4 = M))
//    +- Relation spark_catalog.default.persons_table[id#0,firstName#1,lastName#2,age#3,gender#4] parquet
//
//    == Physical Plan ==
//      *(1) ColumnarToRow
//      +- FileScan parquet spark_catalog.default.persons_table[id#0,firstName#1,lastName#2,age#3,gender#4] Batched: true, Bucketed: false (disabled by query planner), DataFilters: [], Format: Parquet, Location: InMemoryFileIndex(1 paths)[file:/home/culan/workspace/SparkTutorial/spark-warehouse/persons_table..., PartitionFilters: [isnotnull(gender#4), (gender#4 = M)], PushedFilters: [], ReadSchema: struct<id:int,firstName:string,lastName:string,age:int>


    spark.sql("select * from persons_table where gender = 'M' and id = 72").explain(extended = true)

  }

}
