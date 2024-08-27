package com.github.sparktutorial

import com.github.sparktutorial.refdata.Person
import com.trg.crimes.RddTutorialJob.{log, readConfig}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object DataFrameTutorialJob {

  def main(args: Array[String]): Unit = {

    implicit val spark : SparkSession = SparkSession.builder()
      .appName("DataFrameTutorial")
      .getOrCreate()

    implicit val config = readConfig(args)

    log.info(s"running spark tutorial with config $config")
    val personSeq = Seq.tabulate(100){idx =>
      Person(
        idx,
        s"fn $idx",
        s"ln $idx",
        idx % 100,
        if(idx % 2 == 0) "M" else "F"
      )
    }

    //creating df from rdd
    val personsRdd : RDD[Person]= spark.sparkContext.parallelize(personSeq)

    import spark.implicits._
    val personsDataFrame = personsRdd.toDF()
    personsDataFrame.show()

    log.info("spark tutorial run successfully !")
  }

}
