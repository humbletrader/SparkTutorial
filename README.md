# SparkTutorial
Spark tutorial (with scala)

## Existing spark tutorials that worth mention 
 * [sparkcodehum](https://www.sparkcodehub.com/spark-tutorial)

## Spark general knowledge 
 * [difference between rdd, dataframe, dataset](https://stackoverflow.com/questions/31508083/difference-between-dataframe-dataset-and-rdd-in-spark)
 * [What is task, stage, job](https://medium.com/@diehardankush/what-are-job-stage-and-task-in-apache-spark-2fc0d326c15f)
 * [File Formats: Parquet](https://medium.com/@siladityaghosh/understanding-the-parquet-file-format-a-comprehensive-guide-b06d2c4333db)
 * [File Formats: Parquet and Delta Lake](https://youtu.be/1j8SdS7s_NY?feature=shared) 
 * [Tungsten project](https://databricks.com/blog/2015/04/28/project-tungsten-bringing-spark-closer-to-bare-metal.html)
 * Serialization difference beteeen RDD and dataframes : see the markdown in main/scala/sqltutorial/Tungsten.md
 * [Catalist optimizer](https://www.databricks.com/blog/2015/04/13/deep-dive-into-spark-sqls-catalyst-optimizer.html)

## RDDs
* [Difference between rdd.fold and rdd.reduce](https://stackoverflow.com/questions/26634814/why-are-aggregate-and-fold-two-different-apis-in-spark)
* [Difference between foldByKey, reduceByKey, aggregateByKey, combineByKey (also groupByKey)](https://stackoverflow.com/questions/43364432/spark-difference-between-reducebykey-vs-groupbykey-vs-aggregatebykey-vs-combineb)
* [Caching/Persisting/checkpointing](https://medium.com/@john_tringham/spark-concepts-simplified-cache-persist-and-checkpoint-225eb1eef24b)
* [Repartition/Coalesce/PartitionBy](https://medium.com/@vikaskumar.ran/spark-repartition-vs-coalesce-and-when-to-use-which-3f269b47a5dd)


## DataFrames


## Joins
 * [Types of joins](https://www.waitingforcode.com/apache-spark-sql/join-types-spark-sql/read)
 * [Joins Explained](https://medium.com/@amarkrgupta96/join-strategies-in-apache-spark-a-hands-on-approach-d0696fc0a6c9)
 * [Catalyst optimizer: Logical plan, Physical plan, Code generation](https://medium.com/datalex/sparks-logical-and-physical-plans-when-why-how-and-beyond-8cd1947b605a)
 * [Logical and Physical Plan](https://medium.com/datalex/sparks-logical-and-physical-plans-when-why-how-and-beyond-8cd1947b605a)
 * [Video explanation for logical and physical plan](https://youtu.be/GtRGwUUSUB4?feature=shared)

## Spark sql interview questions:
* [Spark SQL interview](https://www.linkedin.com/pulse/spark-sql-sqlhive-commonly-asked-questions-data-ram-ghadiyaram?trk=public_profile_article_view)

## Todo
    * dataset optimizations
    * check if spark sql supports lateral joins 
    * partitonBy vs bucketBy
    * window functions
    * working with columns (especially time/date columns)