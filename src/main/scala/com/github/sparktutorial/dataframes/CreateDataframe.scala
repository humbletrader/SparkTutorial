package com.github.sparktutorial.dataframes

import com.github.sparktutorial.utils.logging.Logging
import org.apache.spark.sql.types.{DataTypes, StructField, StructType}
import org.apache.spark.sql.{Dataset, Row, SparkSession}

import scala.collection.JavaConverters._

object CreateDataframe extends Logging{

  def main(args: Array[String]): Unit = {
    implicit val spark = SparkSession.builder()
      .appName("DataFrameTutorial")
      .getOrCreate()


    val csvDataAsScalaSeq: Seq[String] =
      """
        |ID,FIRST_NAME,LAST_NAME,DESIGNATION,DEPARTMENT,SALARY
        |1001,Ram,Ghadiyaram,Director of Sales,Sales,30000
        |1002,Ravi,Rangasamy,Marketing Manager,Sales,25000
        |1003,Ramesh, Rangasamy,Assistant Manager,Sales,25000
        |1004,Prem,Sure,Account Coordinator,Account,15000
        |1005,Phani ,G,Accountant II,Account,20000
        |1006,Krishna,G,Account Coordinator,Account,15000
        |1007,Rakesh,Krishnamurthy,Assistant Manager,Sales,25000
        |1008,Gally,Johnson,Manager,Account,28000
        |1009,Richard,Grill,Account Coordinator,Account,12000
        |1010,Sofia,Ketty,Sales Coordinator,Sales,20000
        |""".stripMargin.lines().toList.asScala

    sparkInfersTheSchema(csvDataAsScalaSeq)

    //read data by telling spark what is the structure
    userDefinedSchema(csvDataAsScalaSeq)
  }


  private def sparkInfersTheSchema(csvData: Seq[String])
                                  (implicit spark: SparkSession) : Unit = {
    import spark.implicits._
    val datasetOfLines: Dataset[String] = csvData.toDS

    //read data by letting spark to infer the types
    val datasetWithInferredSchema: Dataset[Row] = spark.read
      .option("header", true)
      .option("inferSchema", true)
      .csv(datasetOfLines)

    datasetWithInferredSchema.printSchema()
    datasetWithInferredSchema.show
  }

  private def userDefinedSchema(csvData: Seq[String])
                               (implicit spark: SparkSession) : Unit = {
    val schema = StructType(Array(
      StructField("id", DataTypes.IntegerType),
      StructField("firstName", DataTypes.StringType),
      StructField("lastName", DataTypes.StringType),
      StructField("job", DataTypes.StringType),
      StructField("dept", DataTypes.StringType),
      StructField("salary", DataTypes.IntegerType)
    ))

    val rddOfRows = spark.sparkContext.parallelize(csvData.drop(2))
      .map(line => line.split(","))
      .map(values => Row(values(0).toInt, values(1), values(2), values(3), values(4), values(5).toInt))

    val dataframeWithSchema = spark.createDataFrame(rddOfRows, schema)
    dataframeWithSchema.printSchema()
    dataframeWithSchema.show()
  }

}
