package com.github.sparktutorial

object UserDefinedFunctionsJob {

  def main(args: Array[String]): Unit = {
    // Create a SparkSession
    import org.apache.spark.sql.SparkSession
    val spark = SparkSession.builder()
      .appName("UserDefinedFunctions")
      .getOrCreate()

    // Create a DataFrame
    val data = Seq((1, "John Doe"), (2, "Jane Doe"), (3, "Foo Bar"))
    val columns = Seq("id", "name")
    val df = spark.createDataFrame(data).toDF(columns: _*)

    // Register a UDF
    import org.apache.spark.sql.functions.udf
    val upper: String => String = _.toUpperCase
    val upperUDF = udf(upper)
    val df2 = df.withColumn("name_upper", upperUDF(df("name")))
    df2.show()

    //create a new Udf that takes two arguments
    val concat: (Int, String) => String = _ + " " + _
    val concatUDF = udf(concat)
    val df3 = df2.withColumn("name_upper_concat", concatUDF(df2("id"), df2("name_upper")))
    df3.show()
    df3.explain()

    // Stop the SparkSession
    spark.stop()
  }

}
