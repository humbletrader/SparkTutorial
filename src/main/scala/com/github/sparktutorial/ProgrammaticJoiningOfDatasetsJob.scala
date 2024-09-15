package com.github.sparktutorial

import com.github.sparktutorial.config.SparkTutorialConfigReader
import com.github.sparktutorial.refdata.Generator
import com.github.sparktutorial.utils.logging.Logging
import org.apache.spark.sql.SparkSession


object ProgrammaticJoiningOfDatasetsJob extends SparkTutorialConfigReader with Logging{

  def main(args: Array[String]): Unit = {

    implicit val spark: SparkSession = SparkSession.builder()
      .appName("DataFrameTutorial")
      .getOrCreate()

    implicit val config = readConfig(args)

    log.info(s"running spark tutorial with config $config")

    import spark.implicits._
    import org.apache.spark.sql.functions.to_date
    val personsDS = spark.createDataset(Generator.createPersons())
      .withColumn("birthDate", to_date($"strDateOfBirth", "yyyy-MM-dd"))
      .withColumn("age", Generator.udfComputeAge($"birthDate"))

    personsDS.show
//    val employeesDS = spark.createDataset(Generator.createEmployees())

    //joinType – Type of join to perform.
    // Default inner.
    // Must be one of: inner, cross, outer, full, full_outer, left, left_outer, right, right_outer, left_semi, left_anti.
//    personsDS.join(employeesDS, personsDS("id") === employeesDS("id"), "inner").show
//
//    personsDS.join(employeesDS, personsDS("id") === employeesDS("id"), "outer").show
//    personsDS.join(employeesDS, personsDS("id") === employeesDS("id"), "left_outer").show
//
//    personsDS.join(employeesDS, personsDS("id") === employeesDS("id"), "right_outer").show
//
//    personsDS.join(employeesDS, personsDS("id") === employeesDS("id"), "full_outer").show
//
//    personsDS.join(employeesDS, personsDS("id") === employeesDS("id"), "left_semi").show
//
//    personsDS.join(employeesDS, personsDS("id") === employeesDS("id"), "left_anti").show

  }

}
