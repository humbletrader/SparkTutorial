# SparkTutorial
Spark tutorial (with scala)

## Existing spark tutorials that worth mention 
 * [sparkcodehum](https://www.sparkcodehub.com/spark-tutorial)
 * [mungingdata.com](https://www.mungingdata.com)
 * [The internals of spark](https://books.japila.pl/spark-sql-internals/)

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

## Dataframes/Datasets
 * [Repartition/Coalesce/PartitionBy](https://medium.com/@vikaskumar.ran/spark-repartition-vs-coalesce-and-when-to-use-which-3f269b47a5dd)
 * [Catalyst optimizer: Logical plan, Physical plan, Code generation](https://medium.com/datalex/sparks-logical-and-physical-plans-when-why-how-and-beyond-8cd1947b605a)
 * [Logical and Physical Plan](https://medium.com/datalex/sparks-logical-and-physical-plans-when-why-how-and-beyond-8cd1947b605a)
 * [Video explanation for logical and physical plan](https://youtu.be/GtRGwUUSUB4?feature=shared)   
 * [PartitionBy vs Bucketing](https://medium.com/@paulamaranon/partitionby-vs-bucketing-in-apache-spark-42a3cec2d22f)
 * Disk Partitioning vs. Memory Partitioning : todo
 * [SaveAsTable vs Save](https://medium.com/@tomhcorbin/data-storage-in-pyspark-save-vs-saveastable-8787e9370dde)

## Joins
 * [Types of joins](https://www.waitingforcode.com/apache-spark-sql/join-types-spark-sql/read)
 * [Sort Merge join explained](https://www.waitingforcode.com/apache-spark-sql/sort-merge-join-spark-sql/read)
 * [Joins Explained](https://medium.com/@amarkrgupta96/join-strategies-in-apache-spark-a-hands-on-approach-d0696fc0a6c9)


## Spark sql interview questions:
* [Spark SQL interview](https://www.linkedin.com/pulse/spark-sql-sqlhive-commonly-asked-questions-data-ram-ghadiyaram?trk=public_profile_article_view)


## Extras: 
* [working with time functions in datasets/dataframes](https://www.mungingdata.com/apache-spark/week-end-start-dayofweek-next-day/#dayofweek)

## Todo
* check if spark sql supports lateral joins 
* partitonBy vs bucketBy
* window functions
* data lakes
* databricks
   
    