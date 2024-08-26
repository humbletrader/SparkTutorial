package com.github.sparktutorial


package object config {

  object SparkTutorialConfig{
  }

  case class SparkTutorialConfig(
    rawInput : String = "./input",
    parquetOoutputPath : String = "./output/parquet",
    jsonOutputPath : String = "./output/json"
   ){
  }
}
