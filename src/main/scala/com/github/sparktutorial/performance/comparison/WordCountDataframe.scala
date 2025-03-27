package com.github.sparktutorial.performance.comparison

// WordCountDataFrame_Memory.scala
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object WordCountDataframe {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("DataFrame Word Count In-Memory")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._

    val lines = Seq.fill(100000)("this is a test of spark performance")
    val df = spark.createDataset(lines)

    val start = System.nanoTime()
    val wordCounts = df
      .select(explode(split($"value", "\\s+")).alias("word"))
      .groupBy("word")
      .count()
      .collect()
    val duration = (System.nanoTime() - start) / 1e9d

    println("DataFrame Result: " + wordCounts.mkString(", "))
    println(s"DataFrame Execution Time: $duration seconds")

    spark.stop()
  }
}

