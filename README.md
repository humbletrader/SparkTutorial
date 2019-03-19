# SparkTutorial
Spark tutorial (with scala)


# Put this in jupyter

```scala
val firstNames = Array("aaaa", "bbbb", "cccc", "ddddd")
val lastNames = Array("aaa", "bbb", "ccc")

//testing the zip operation
//val firstAndLastNames = sc.parallelize(firstNames).zip(sc.parallelize(lastNames))
//org.apache.spark.SparkException: Can only zip RDDs with same number of elements in each partition

val firstNames = Map(1 -> "aaaa", 2 -> "bbbb", 3 -> "cccc", 4 -> "DDDD")
val lastNames = Map(1 -> "aaa", 2 -> "bbb", 3 -> "ccc", 5 -> "EEE")

val parFirstNames = sc.parallelize(firstNames.toSeq)
val parLastNames = sc.parallelize(lastNames.toSeq)

val joined = parFirstNames.join(parLastNames)

//result  Array((1,(aaaa,aaa)), (2,(bbbb,bbb)), (3,(cccc,ccc)))

val leftJoined = parallelFirstNames.leftOuterJoin(parallelLastNames)
//Array((4,(DDDD,None)), (1,(aaaa,Some(aaa))), (2,(bbbb,Some(bbb))), (3,(cccc,Some(ccc))))

val rightJoined = parallelFirstNames.rightOuterJoin(parallelLastNames)
//Array((1,(Some(aaaa),aaa)), (5,(None,EEE)), (2,(Some(bbbb),bbb)), (3,(Some(cccc),ccc)))

val fullJoined = parFirstNames.fullOuterJoin(parLastNames)
//Array((4,(Some(DDDD),None)), (1,(Some(aaaa),Some(aaa))), (5,(None,Some(EEE))), (2,(Some(bbbb),Some(bbb))), (3,(Some(cccc),Some(ccc))))
```

[Difference between rdd.fold and rdd.reduce](https://stackoverflow.com/questions/26634814/why-are-aggregate-and-fold-two-different-apis-in-spark)

[Difference between foldByKey, reduceByKey, aggregateByKey, combineByKey (also groupByKey)](https://stackoverflow.com/questions/43364432/spark-difference-between-reducebykey-vs-groupbykey-vs-aggregatebykey-vs-combineb)

[What is task, stage, job]()

[difference between rdd, dataframe, dataset]


