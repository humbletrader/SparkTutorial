package com.github.sparktutorial.datasets


import org.apache.spark.sql.{SparkSession, DataFrame}
import org.apache.spark.sql.functions._
import scala.util.Random


object PerformanceBenchmark {


  def main(args: Array[String]): Unit = {


    // Initialize Spark Session
    val spark = SparkSession.builder()
      .appName("Join Performance Benchmark")
      .master("local[*]") // Adjust for cluster usage
      .config("spark.sql.autoBroadcastJoinThreshold", "-1") // Disable broadcast joins
      .enableHiveSupport() // Required for bucketed tables
      .getOrCreate()

    import spark.implicits._

    // Generate Sample Data (Simulating Large Dataset)
    val numRecords = 10000000 // 10 million rows

    // Users DataFrame (1M unique user_id)
    val usersDF = (1 to numRecords / 10).map(i => (i, s"User_$i")).toDF("user_id", "name")

    // Transactions DataFrame (10M records, random user_id matches)
    val transactionsDF = (1 to numRecords).map(_ =>
      (Random.nextInt(numRecords / 10) + 1, Random.nextInt(1000))
    ).toDF("user_id", "amount")

    // Function to Measure Execution Time
    def time[T](block: => T, label: String): (T, Double) = {
      val start = System.nanoTime()
      val result = block
      val duration = (System.nanoTime() - start) / 1e9
      println(s"$label Execution Time: $duration seconds")
      (result, duration)
    }

    // **1️⃣ Default Join (Spark Shuffle)**
    val (_, defaultJoinTime) = time({
      usersDF.join(transactionsDF, "user_id").count()
    }, "Default Join (Spark Shuffle)")

    // **2️⃣ Pre-Partitioned Join (Minimizing Shuffle)**


    val (_, partitionedJoinTime) = time({
      val usersPartitioned = usersDF.repartition($"user_id")
      val transactionsPartitioned = transactionsDF.repartition($"user_id")
      usersPartitioned.join(transactionsPartitioned, "user_id").count()
    }, "Pre-Partitioned Join")


    // **4️⃣ Display Results**
    val results = Seq(
      ("Default Join (Spark Shuffle)", defaultJoinTime),
      ("Pre-Partitioned Join", partitionedJoinTime),

    ).toDF("Join Strategy", "Execution Time (seconds)")

    // Show benchmark results
    results.show()
  }
}