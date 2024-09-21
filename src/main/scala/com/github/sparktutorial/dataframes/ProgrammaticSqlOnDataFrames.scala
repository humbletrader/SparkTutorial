package com.github.sparktutorial.dataframes

import com.github.sparktutorial.config.SparkTutorialConfigReader
import com.github.sparktutorial.refdata.PersonWithAge
import com.github.sparktutorial.utils.logging.Logging
import org.apache.spark.sql.SparkSession

object ProgrammaticSqlOnDataFrames extends SparkTutorialConfigReader with Logging{

  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder()
      .appName("DataFrameTutorial")
      .getOrCreate()

    implicit val config = readConfig(args)

    log.info(s"running spark tutorial with config $config")
    val personSeq = Seq.tabulate(100) { idx =>
      PersonWithAge(
        idx,
        s"fn $idx",
        s"ln $idx",
        idx % 100,
        if (idx % 2 == 0) "M" else "F"
      )
    }

    import spark.implicits._
    val personsDF = personSeq.toDF()

    //compute average age by sex
    personsDF.select(personsDF("gender"), personsDF("age"))
      .groupBy(personsDF("gender"))
      .avg("age")
      .show()

  }//end main

}
