package com.github.sparktutorial

object ParquetFeatures {

  case class PersonAndAddress(
    id: Int,
    name: String,
    address: Address
  )

  case class Address(
    street: String,
    city: String,
    state: String,
    zip: String,
    secretInfo: SecretInfo
  )

  case class SecretInfo(
    secret: String,
    hidden: List[String]
  )

  def main(args: Array[String]) : Unit = {
    import org.apache.spark.sql.SparkSession
    val spark = SparkSession.builder()
      .appName("ParquetFeatures")
      .getOrCreate()

    import spark.implicits._
    val data = Seq(
      PersonAndAddress(1, "John Doe", Address("123 Main St", "Springfield", "IL", "62701", SecretInfo("secret1", List("hidden1", "hidden2")))),
      PersonAndAddress(2, "Jane Doe", Address("456 Main St", "Springfield", "IL", "62701", SecretInfo("secret1", List("hidden1", "hidden2")))),
      PersonAndAddress(3, "Foo Bar", Address("789 Main St", "Springfield", "IL", "62701", SecretInfo("secret1", List("hidden1", "hidden2"))))
    )
    val df = spark.createDataFrame(data)
    df.show()

    df.write.mode("overwrite").parquet("./output/complexdatastructure")

    val dfFromDisk = spark.read.parquet("./output/complexdatastructure")
    dfFromDisk.show()

    spark.stop()
  }
}
