package com.github.sparktutorial.performance.comparison

// WordCountRDD_Memory.scala
import org.apache.spark.{SparkConf, SparkContext}

object WordCountRdd {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("RDD Word Count In-Memory").setMaster("local[*]")
    val sc = new SparkContext(conf)

    val lines = Seq.fill(100000)("this is a test of spark performance")
    val rdd = sc.parallelize(lines)

    val start = System.nanoTime()
    val wordCounts = rdd
      .flatMap(_.split("\\s+"))
      .map(word => (word, 1))
      .reduceByKey(_ + _)
      .collect()
    val duration = (System.nanoTime() - start) / 1e9d

    println("RDD Result: " + wordCounts.mkString(", "))
    println(s"RDD Execution Time: $duration seconds")

    sc.stop()
  }
}

