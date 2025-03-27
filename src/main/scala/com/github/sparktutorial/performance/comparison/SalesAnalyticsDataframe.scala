package com.github.sparktutorial.performance.comparison

// SalesAnalyticsDF.scala
import org.apache.spark.sql.{SparkSession, functions => F}
import scala.util.Random

object SalesAnalyticsDataframe {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("Sales Analytics DF")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._

    // Simulated Data
    val transactions = Seq.fill(10000000)(
      (Random.nextInt(1000), // transactionId
        Random.nextInt(100),  // customerId
        Random.nextDouble() * 1000, // amount
        Seq("US", "UK", "DE", "IN", "CA")(Random.nextInt(5)) // country
      )
    ).toDF("transactionId", "customerId", "amount", "country")

    val customers = (0 until 10000000).map(id =>
      (id, s"Customer$id", 18 + Random.nextInt(50), Seq("M", "F")(Random.nextInt(2)))
    ).toDF("customerId", "name", "age", "gender")

    val start = System.nanoTime()

    val joined = transactions.join(customers, "customerId")
      .filter($"age" > 30)
      .groupBy("country", "gender")
      .agg(F.sum("amount").alias("total_amount"))

    val result = joined.collect()
    val duration = (System.nanoTime() - start) / 1e9d

    println("DataFrame Result: " + result.mkString(", "))
    println(f"DataFrame Execution Time: $duration%.3f seconds")

    spark.stop()
  }
}

