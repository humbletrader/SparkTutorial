package com.github.sparktutorial

import com.github.sparktutorial.config.SparkTutorialConfigReader
import com.github.sparktutorial.utils.logging.Logging
import org.apache.spark.sql.SparkSession

import scala.collection.JavaConverters._

object SqlTutorialJob extends SparkTutorialConfigReader with Logging {

  def main(args: Array[String]): Unit = {

    implicit val spark: SparkSession = SparkSession.builder()
      .appName("DataFrameTutorial")
      .getOrCreate()

    implicit val config = readConfig(args)

    val csvData = spark.sparkContext.parallelize(
      """
        |ID,FIRST_NAME,LAST_NAME,DESIGNATION,DEPARTMENT,SALARY
        |1001,Ram,Ghadiyaram,Director of Sales,Sales,30000
        |1002,Ravi,Rangasamy,Marketing Manager,Sales,25000
        |1003,Ramesh, Rangasamy,Assistant Manager,Sales,25000
        |1004,Prem,Sure,Account Coordinator,Account,15000
        |1005,Phani ,G,Accountant II,Account,20000
        |1006,Krishna,G,Account Coordinator,Account,15000
        |1007,Rakesh,Krishnamurthy,Assistant Manager,Sales,25000
        |1008,Gally,Johnson,Manager,Account,28000
        |1009,Richard,Grill,Account Coordinator,Account,12000
        |1010,Sofia,Ketty,Sales Coordinator,Sales,20000
        |""".stripMargin.lines.toList.asScala)

    import spark.implicits._
    val csvLinesDataset = csvData.toDS()

    val frame = spark.read.option("header", true).option("inferSchema", true).csv(csvLinesDataset)
    frame.show
    frame.schema
    frame.createOrReplaceTempView("emp_dept_table")


    log.info("select the 5th largest salary")
    val sqlResultDataFrame = spark.sql(
      """
        |select * from (
        | select first_name, last_name, department, salary, dense_rank() over(order by salary desc) as rank from emp_dept_table
        |) where rank = 5
        |""".stripMargin)

    sqlResultDataFrame.show()
    sqlResultDataFrame.explain()

    //24/08/27 19:23:06 WARN WindowExec: No Partition Defined for Window operation! Moving all data to a single partition,
    // this can cause serious performance degradation.
    //== Physical Plan ==
    //AdaptiveSparkPlan isFinalPlan=false
    //+- Filter (rank#51 = 5)
    //   +- Window [dense_rank(salary#19) windowspecdefinition(salary#19 DESC NULLS LAST, specifiedwindowframe(RowFrame, unboundedpreceding$(), currentrow$())) AS rank#51], [salary#19 DESC NULLS LAST]
    //      +- Sort [salary#19 DESC NULLS LAST], false, 0
    //         +- Exchange SinglePartition, ENSURE_REQUIREMENTS, [plan_id=138]
    //            +- Project [first_name#15, last_name#16, department#18, salary#19]
    //               +- Scan ExistingRDD[ID#14,FIRST_NAME#15,LAST_NAME#16,DESIGNATION#17,DEPARTMENT#18,SALARY#19]

    log.info("select the 2nd largest salary with partition on department")
    val rankWithPartition = spark.sql(
      """
        |select * from (
        | select first_name, last_name, department, salary, dense_rank() over(partition by department order by salary desc) as rank from emp_dept_table
        |) where rank = 2
        |""".stripMargin)

    rankWithPartition.show()

  }
}
