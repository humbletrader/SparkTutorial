package com.github.sparktutorial.datasets

import com.github.sparktutorial.config.SparkTutorialConfigReader
import com.github.sparktutorial.refdata.Generator
import com.github.sparktutorial.utils.logging.Logging
import org.apache.spark.sql.SparkSession


object ProgrammaticJoining extends SparkTutorialConfigReader with Logging{

  def main(args: Array[String]): Unit = {

    implicit val spark: SparkSession = SparkSession.builder()
      .appName("DataFrameTutorial")
      .getOrCreate()

    implicit val config = readConfig(args)

    log.info(s"running spark tutorial with config $config")

    import spark.implicits._
    val personsDS = spark.createDataset(Generator.createPersons())
    personsDS.show()

    val employeesDS = spark.createDataset(Generator.createEmployees())
    employeesDS.show()

    //joinType – Type of join to perform. Default inner.
    // Must be one of:
    //      inner, cross, outer, full,
    //      fullouter, full_outer,
    //      left, leftouter, left_outer,
    //      right, rightouter, right_outer,
    //      semi, leftsemi, left_semi,
    //      anti, leftanti, left_anti.
    personsDS.join(employeesDS, personsDS("id") === employeesDS("id"), "inner").show

    //outer join = outer
    personsDS.join(employeesDS, personsDS("id") === employeesDS("id"), "outer").show
    personsDS.join(employeesDS, personsDS("id") === employeesDS("id"), "left_outer").show

    //right outer
    personsDS.join(employeesDS, personsDS("id") === employeesDS("id"), "right_outer").show

    //full outer
    personsDS.join(employeesDS, personsDS("id") === employeesDS("id"), "full_outer").show

    personsDS.join(employeesDS, personsDS("id") === employeesDS("id"), "left_semi").show

    personsDS.join(employeesDS, personsDS("id") === employeesDS("id"), "left_anti").show

    //cross join
    personsDS.crossJoin(employeesDS)

    //all the above have returned dataframes, if you need to keep the dataset you need
    personsDS.joinWith(employeesDS, personsDS("id") === employeesDS("id")).show

  }

}
