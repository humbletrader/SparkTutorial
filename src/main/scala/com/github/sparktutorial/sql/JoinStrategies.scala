package com.github.sparktutorial.sql

import com.github.sparktutorial.config.SparkTutorialConfigReader
import com.github.sparktutorial.refdata.Generator
import com.github.sparktutorial.utils.logging.Logging
import org.apache.spark.sql.SparkSession

object JoinStrategies extends SparkTutorialConfigReader with Logging {

  def main(args: Array[String]): Unit = {

    implicit val spark: SparkSession = SparkSession.builder()
      .appName("JoinStrategies")
      .getOrCreate()

    implicit val config = readConfig(args)

    import spark.implicits._
    val personsDS = spark.createDataset(Generator.createPersons())
    personsDS.show()

    val employeesDS = spark.createDataset(Generator.createEmployees())

    employeesDS.show()

    //see the broadcast join below
    personsDS.join(employeesDS, personsDS("id") === employeesDS("id"), "inner").explain()
    //== Physical Plan ==
    // AdaptiveSparkPlan isFinalPlan=false
    //  +- BroadcastHashJoin [id#5], [id#42], Inner, BuildRight, false
    //    :- LocalTableScan [id#5, firstName#6, lastName#7, gender#8, strDateOfBirth#9]
    //    +- BroadcastExchange HashedRelationBroadcastMode(List(cast(input[0, int, false] as bigint)),false), [plan_id=18]
    //      +- LocalTableScan [id#42, firstName#43, lastName#44, department#45, salary#46]

    //forcing a shufle hash join
    personsDS.join(employeesDS.hint("shuffle_hash"), personsDS("id") === employeesDS("id"), "inner").explain()
    //== Physical Plan ==
    //AdaptiveSparkPlan isFinalPlan=false
    //  +- ShuffledHashJoin [id#5], [id#42], Inner, BuildRight
    //     :- Exchange hashpartitioning(id#5, 200), ENSURE_REQUIREMENTS, [plan_id=33]
    //     :  +- LocalTableScan [id#5, firstName#6, lastName#7, gender#8, strDateOfBirth#9]
    //  +- Exchange hashpartitioning(id#42, 200), ENSURE_REQUIREMENTS, [plan_id=34]
    //     +- LocalTableScan [id#42, firstName#43, lastName#44, department#45, salary#46]

    //other dataset join hints: broadcast, merge, shuffle_hash, shuffle_replicate_nl = cartesian product
  }
}