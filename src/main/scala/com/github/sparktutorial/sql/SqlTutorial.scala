package com.github.sparktutorial.sql

import com.github.sparktutorial.config.SparkTutorialConfigReader
import com.github.sparktutorial.utils.logging.Logging
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.execution.ExtendedMode

import scala.collection.JavaConverters._

object SqlTutorial extends SparkTutorialConfigReader with Logging {

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
        |1011,Gigi,Berlogea,Software Engineer,IT,25000
        |""".stripMargin.lines.toList.asScala)

    import spark.implicits._
    val csvLinesDataset = csvData.toDS()

    val frame = spark.read.option("header", true).option("inferSchema", true).csv(csvLinesDataset)
    frame.show
    frame.schema
    frame.createOrReplaceTempView("emp_dept_table")


    log.info("using window functions in spark...")
    val windowFunctions = spark.sql(
      """
        |select first_name, last_name, department, salary, dense_rank() over(partition by department order by salary desc) as rank from emp_dept_table
        |""".stripMargin)
    windowFunctions.show()

    log.info("select the 5th largest salary in each department")
    val sqlResultDataFrame = spark.sql(
      """
        |select * from (
        |   select first_name, last_name, department, salary, dense_rank() over(order by salary desc) as rank from emp_dept_table
        |) where rank = 5
        |""".stripMargin
    )

    sqlResultDataFrame.show()
    sqlResultDataFrame.explain(ExtendedMode.name)

    log.info("select the 2nd largest salary for each department")
    val rankWithPartition = spark.sql(
      """
        |select * from (
        | select first_name, last_name, department, salary, dense_rank() over(partition by department order by salary desc) as rank from emp_dept_table
        |) where rank = 2
        |""".stripMargin
    )
    rankWithPartition.show()

    log.info("select duplicate salaries")
    val duplicateSalaries = spark.sql(
      """
        | select * from (
        |   select first_name, last_name, department, salary, count(salary) over(partition by salary) as count_salary
        |   from emp_dept_table
        | ) where count_salary > 1
        |""".stripMargin
    )
    duplicateSalaries.show()
  }
}
