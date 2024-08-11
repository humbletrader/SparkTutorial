val firstNames = Array("aaaa", "bbbb", "cccc", "ddddd")
val lastNames = Array("aaa", "bbb", "ccc")

//testing the zip operation
//val firstAndLastNames = sc.parallelize(firstNames).zip(sc.parallelize(lastNames))
//org.apache.spark.SparkException: Can only zip RDDs with same number of elements in each partition

val firstNames = Map(1 -> "aaaa", 2 -> "bbbb", 3 -> "cccc", 4 -> "DDDD")
val lastNames = Map(1 -> "aaa", 2 -> "bbb", 3 -> "ccc", 5 -> "EEE")

val rddFirstNames = sc.parallelize(firstNames.toSeq)
val rddLastNames = sc.parallelize(lastNames.toSeq)

val joined = rddFirstNames.join(rddLastNames)

//result  Array((1,(aaaa,aaa)), (2,(bbbb,bbb)), (3,(cccc,ccc)))

val leftJoined = rddFirstNames.leftOuterJoin(rddLastNames)
//Array((4,(DDDD,None)), (1,(aaaa,Some(aaa))), (2,(bbbb,Some(bbb))), (3,(cccc,Some(ccc))))

val rightJoined = rddFirstNames.rightOuterJoin(rddLastNames)
//Array((1,(Some(aaaa),aaa)), (5,(None,EEE)), (2,(Some(bbbb),bbb)), (3,(Some(cccc),ccc)))

val fullJoined = rddFirstNames.fullOuterJoin(rddLastNames)
//Array((4,(Some(DDDD),None)), (1,(Some(aaaa),Some(aaa))), (5,(None,Some(EEE))), (2,(Some(bbbb),Some(bbb))), (3,(Some(cccc),Some(ccc))))


//rdd operations:  toDF - rdd to dataframe
rddFirstNames.toDF