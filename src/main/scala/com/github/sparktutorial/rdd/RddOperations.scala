package com.github.sparktutorial.rdd

import com.github.sparktutorial.config.SparkTutorialConfigReader
import com.github.sparktutorial.utils.logging.Logging
import org.apache.spark.TaskContext
import org.apache.spark.sql.SparkSession

object RddOperations extends SparkTutorialConfigReader with Logging {

  def main(args: Array[String]): Unit = {

    implicit val spark : SparkSession = SparkSession.builder()
      .appName("RddTutorial")
      .getOrCreate()
    implicit val config = readConfig(args)

    val sc = spark.sparkContext

    log.info(s"running spark tutorial with config $config")

    //val firstNames = Array("aaaa", "bbbb", "cccc", "ddddd")
    //val lastNames = Array("aaa", "bbb", "ccc")
    //testing the zip operation
    //val firstAndLastNames = sc.parallelize(firstNames).zip(sc.parallelize(lastNames))
    //org.apache.spark.SparkException: Can only zip RDDs with same number of elements in each partition

    val firstNames = 1 to 10000 map { i => (i, s"first_name $i") }
    val lastNames = Map(1 -> "aaa", 2 -> "bbb", 3 -> "ccc", 5 -> "EEE")

    val rddFirstNames = sc.parallelize(firstNames, 50)
    val rddLastNames = sc.parallelize(lastNames.toSeq)

    val joined = rddFirstNames.join(rddLastNames)
    println(joined.collect.mkString("\n"))
    //result  Array((1,(aaaa,aaa)), (2,(bbbb,bbb)), (3,(cccc,ccc)))

    val leftJoined = rddFirstNames.leftOuterJoin(rddLastNames)
    println(leftJoined.collect.mkString("\n"))
    //Array((4,(DDDD,None)), (1,(aaaa,Some(aaa))), (2,(bbbb,Some(bbb))), (3,(cccc,Some(ccc))))

    val rightJoined = rddFirstNames.rightOuterJoin(rddLastNames)
    println(rightJoined.collect.mkString("\n"))
    //Array((1,(Some(aaaa),aaa)), (5,(None,EEE)), (2,(Some(bbbb),bbb)), (3,(Some(cccc),ccc)))

    val fullJoined = rddFirstNames.fullOuterJoin(rddLastNames)
    println(fullJoined.collect().mkString("\n"))
    //Array((4,(Some(DDDD),None)), (1,(Some(aaaa),Some(aaa))), (5,(None,Some(EEE))), (2,(Some(bbbb),Some(bbb))), (3,(Some(cccc),Some(ccc))))

    //rdd operations: co-group
    val cogrouped = rddFirstNames.cogroup(rddLastNames)
    println(cogrouped.collect().mkString("\n"))
    //(4,(CompactBuffer(DDDD),CompactBuffer()))
    //(1,(CompactBuffer(aaaa),CompactBuffer(aaa)))
    //(5,(CompactBuffer(),CompactBuffer(EEE)))
    //(2,(CompactBuffer(bbbb),CompactBuffer(bbb)))
    //(3,(CompactBuffer(cccc),CompactBuffer(ccc)))

    sc.stop()

    log.info("spark tutorial run successfully !")
  }
}
