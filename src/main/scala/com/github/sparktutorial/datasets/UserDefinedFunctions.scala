package com.github.sparktutorial.datasets

import ProgrammaticJoining.{log, readConfig}
import com.github.sparktutorial.refdata.Generator
import com.github.sparktutorial.refdata.Generator.computeAge
import com.github.sparktutorial.utils.logging.Logging
import org.apache.spark.sql.SparkSession

object UserDefinedFunctions extends Logging{

  def main(args: Array[String]) : Unit = {
    implicit val spark: SparkSession = SparkSession.builder()
      .appName("DataFrameTutorial")
      .getOrCreate()

    implicit val config = readConfig(args)

    log.info(s"running spark tutorial with config $config")

    import spark.implicits._
    import org.apache.spark.sql.functions.to_date
    val udfComputeAge = org.apache.spark.sql.functions.udf(computeAge _)

    val personsDS = spark.createDataset(Generator.createPersons())
      .withColumn("birthDate", to_date($"strDateOfBirth", "yyyy-MM-dd"))
      .withColumn("age", udfComputeAge($"birthDate"))

    personsDS.show
  }

}
