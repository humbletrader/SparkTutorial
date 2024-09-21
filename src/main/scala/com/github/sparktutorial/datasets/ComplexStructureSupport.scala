package com.github.sparktutorial.datasets

import com.github.sparktutorial.refdata.{Address, Person, PersonWithAddress, SecretInfo}
import com.github.sparktutorial.utils.logging.Logging
import org.apache.spark.sql.SparkSession

object ComplexStructureSupport extends Logging{

  def main(args: Array[String]) : Unit ={
    val spark = SparkSession.builder().appName("ComplexStructureSupport").getOrCreate()

    val complexObjectSequence = Seq(
      PersonWithAddress(
        Person(1, "first", "last", "M", "2000-06-28"),
        Address("USA", "New York", "Sunset Ave", 56, SecretInfo("lkjadal", Map("secretWay" -> "tunnel 1 -> tunnel 2")))
      )
    )

    import spark.implicits._
    val datasetWithComplexStructure = complexObjectSequence.toDS

    datasetWithComplexStructure.printSchema()

    datasetWithComplexStructure
      .select($"person.firstName", $"address.country", $"address.street", $"address.secret.secretKey")
      .show()

    datasetWithComplexStructure.write.mode("overwrite").parquet("./output/complexdatastructure")

    val dfFromDisk = spark.read.parquet("./output/complexdatastructure")
    dfFromDisk.show()
    dfFromDisk.select("person.firstName", "address.city", "address.secret.secretKey").show()

    val dataframeWithComplexStructure = datasetWithComplexStructure.toDF()
    dataframeWithComplexStructure.printSchema()

    //dataframe back to dataset
    dataframeWithComplexStructure.as[PersonWithAddress]

  }

}
