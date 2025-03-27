package com.github.sparktutorial.performance.comparison

// SalesAnalyticsRDD.scala
import org.apache.spark.{SparkConf, SparkContext}

object SalesAnalyticsRDD {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("Sales Analytics RDD").setMaster("local[*]")
    val sc = new SparkContext(conf)

    val inMemoryData = Seq.fill(10000000)(
      (scala.util.Random.nextInt(1000), // transactionId
        scala.util.Random.nextInt(100),  // customerId
        scala.util.Random.nextDouble() * 1000, // amount
        Seq("US", "UK", "DE", "IN", "CA")(scala.util.Random.nextInt(5)) // country
      )
    )

    // Simulated data
    val transactions = sc.parallelize(inMemoryData)

    val transactionsWithKey = transactions.keyBy{case (_, custId, _, _) => custId}.mapValues{case (_, _, amount, country) => (amount, country)}

    val customers = sc.parallelize((0 until 10000000).map(id =>
      (id, s"Customer$id", 18 + scala.util.Random.nextInt(50), // age
        Seq("M", "F")(scala.util.Random.nextInt(2)) // gender
      )
    ))

    val customersWithKey = customers.keyBy{case (custId, _, _, _) => custId}.mapValues{case(_, _, age, gender) => (age, gender)}

    val start = System.nanoTime()

    // Join customers to transactions
    val joined = transactionsWithKey.join(customersWithKey)

    val filtered = joined
      .filter { case (_, ((_, _), (age, _))) => age > 30 }

    val result = filtered
      .map { case (_, ((amount, country), (_, gender))) => ((country, gender), amount) }
      .reduceByKey(_ + _)
      .collect()

    val duration = (System.nanoTime() - start) / 1e9d

    println("RDD Result: " + result.mkString(", "))
    println(f"RDD Execution Time: $duration%.3f seconds")

    sc.stop()
  }
}

