package com.github.sparktutorial

import com.github.sparktutorial.utils.logging.Logging
import org.apache.spark.storage.StorageLevel

import java.time.LocalDateTime

object SamplesReader extends Logging {

  case class Position(originalEventTime: LocalDateTime, placement: Int, maxDuration: Int)

  case class Download(positions : Seq[Position],
                      station: Int,
                      stationName: String,
                      agency: Int,
                      language: String,
                      referer: String,
                      domain: String,
                      ip: String,
                      isp: String,
                      commonUrlSegments: Map[String, Seq[String]]
                     )


  def main(args: Array[String]) : Unit = {
    import org.apache.spark.sql.SparkSession
    val spark = SparkSession.builder()
      .appName("SamplesReader")
      .getOrCreate()

    import spark.implicits._
    val datasetInput = spark.read
      .parquet("/home/dragos/workspace/if-sampler/output_parquet/")
    datasetInput.show()
    datasetInput.printSchema()

    import org.apache.spark.sql.functions.col
    val latestVersion = datasetInput.select("timestamp")
      .distinct()
      .where("client = 'chimay'")
      .orderBy(col("timestamp").desc)
      .first()
      .getInt(0)
    log.info("################################ latest version " + latestVersion)

    val downloadsDataset = datasetInput.where("client = 'chimay' and timestamp = '" + latestVersion + "'").as[Download]
    downloadsDataset.persist(StorageLevel.DISK_ONLY)
    downloadsDataset.show()
    downloadsDataset.printSchema()
    log.info("################################ datasetInput count " + downloadsDataset.count())

    import org.apache.spark.sql.functions.explode
    val positionsDataset = datasetInput.select(explode($"positions"))
    positionsDataset.show()
    log.info("################################ positions count " + positionsDataset.count())

    datasetInput.select("positions.originalEventTime").show()
    log.info("################################ positions.eventTime count " + datasetInput.select("positions.originalEventTime").count())

    datasetInput.createTempView("sampled_downloads")
    spark.sql("SELECT * FROM sampled_downloads").show()
    spark.sql("select exploded_positions, station, explode(commonUrlSegments) from (SELECT explode(positions) as exploded_positions, station, commonUrlSegments FROM sampled_downloads WHERE station = 11 )").show()
    spark.sql(
      """select exploded_positions, station, explode(commonUrlSegments)
        |from (
        |   SELECT explode(positions) as exploded_positions, station, commonUrlSegments
        |   FROM sampled_downloads
        |   WHERE station = 11
        |)""".stripMargin
        ).show()

    spark.stop()
  }

}
