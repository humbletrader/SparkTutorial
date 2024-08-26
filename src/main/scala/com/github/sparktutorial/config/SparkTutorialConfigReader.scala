package com.github.sparktutorial.config

import com.github.sparktutorial.config.SparkTutorialConfigReader.{InputConfigId, JsonOutputId, ParquetOutputId}
import com.github.sparktutorial.utils.logging.Logging


/**
 * Poor man's configuration reader
 */
object SparkTutorialConfigReader{
  val InputConfigId = "raw.input="
  val ParquetOutputId = "parquet.output="
  val JsonOutputId = "json.output="
}

/**
 * should be able to parse a configuration like:
 *
 * raw.input=./input
 * parquet.output=./output/parquet
 * json.output=./output/json
 */
trait SparkTutorialConfigReader { self : Logging =>

  def readConfig(args: Array[String]) : SparkTutorialConfig = {
    log.info(s"reading configuration from args ${args.mkString(",")}")

    args.foldLeft(SparkTutorialConfig()){ case (config, newConfigValue) =>
      newConfigValue match {
        case value if value.startsWith(InputConfigId) => config.copy(rawInput = value.substring(InputConfigId.length))
        case value if value.startsWith(ParquetOutputId) => config.copy(parquetOoutputPath = value.substring(ParquetOutputId.length))
        case value if value.startsWith(JsonOutputId) => config.copy(jsonOutputPath = value.substring(JsonOutputId.length))
        case _ => config
      }
    }
  }



}
