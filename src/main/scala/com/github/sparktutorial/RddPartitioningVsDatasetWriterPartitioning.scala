package com.github.sparktutorial

import com.github.sparktutorial.utils.logging.Logging
import org.apache.spark.sql.Dataset

/**
 * we emphasize the difference between partitioning an RDD and partitioning a dataset when writing it to disk
 */
object RddPartitioningVsDatasetWriterPartitioning extends Logging{

  case class Person(age: Int, employed: Boolean, name: String)

  class ByAgePartitioner extends org.apache.spark.Partitioner {
    override def numPartitions: Int = 4 //0-19, 20-29, 30-39, 40-99
    override def getPartition(key: Any): Int = {
      val k = key.asInstanceOf[Int]
      if (k < 20) 0
      else if (k < 30) 1
      else if (k < 40) 2
      else 3
    }
  }

  def main(args: Array[String]): Unit = {
    // Create a SparkSession
    import org.apache.spark.sql.SparkSession
    // Import implicits

    val spark = SparkSession.builder().appName("DatasetFromRdd").getOrCreate()

    val rdd = spark.sparkContext.parallelize(
      Seq(
        Person(11, false, "Teenager Joe"), Person(22, false, "Twenty Something Unemployed"), Person(33, true, "Thirty Something"), Person(44, false, "Forty Something Unemployed"),
        Person(9, true, "Working Child"), Person(27, true, "Twenty Something Jr"), Person(35, false, "Thirty Something Unemployed"), Person(45, true, "Forty Something")
      )
    )
    //partition by age - this will create 4 partitions
    val rddPartitionedByAge = rdd.keyBy(_.age).partitionBy(new ByAgePartitioner)

    //create dataset from an already partitioned RDD
    import spark.implicits._
    val partitionedDataset = spark.createDataset(rddPartitionedByAge.values)
    partitionedDataset
      .write
      .mode("overwrite")
      .partitionBy("employed") //partition by employment status - totally different from the age partitioning
      .csv("./output/datasetFromRdd")

    //please note that the partitioning of the dataset is different from the partitioning of the RDD
    //1. the RDD partitioning ( 4 partitions ) are inherited by the dataset
    //2. the dataset  is further partitioned by the employed column at write time so for employed=true and employed=false
    // we will have 4 output files each ( 4 partitions write in 2 directories = 8 output files)


    //now let's try to read the data back but controlling the partitioning of the dataset
    val rddWithFourPartitions = spark.sparkContext.parallelize(Seq(0, 1, 2, 3), 4)
    //transform rdd to dataset
    val datasetWithFourPartitions = spark.createDataset(rddWithFourPartitions)
    //datasetWithFourPartitions.transform(t => Dataset(t.sparkSession, t.queryExecution, t.encoder))

    val dataset = spark.read.option("header", "true").csv("./output/datasetFromRdd")

    // Stop the SparkSession
    spark.stop()
  }
}
