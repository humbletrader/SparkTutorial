case class Person(firstName: String, lastName: String, age: Int, gender: String, salary: Double)
val personIter = Iterator.tabulate(100){idx => 
    Person(
        s"firstName $idx", 
        s"lastName $idx", 
        idx, 
        if(idx % 2 == 0) "M" else "F", 
        scala.util.Random.nextDouble * 1000
    )
}

//creating df from rdd
val personRdd = sc.parallelize(personIter.toSeq)
val personDf = personRdd.toDF()

//creating df from Seq
import spark.implicits._
val dfFromSeq = personIter.toSeq.toDF()

//writing to parquet
personDf.write.partitionBy("gender").parquet("/tmp/test/persons.parquet")

//reading from parquet
val personsFromDisk = spark.read.parquet("/tmp/test/personsp.parquet")


//selecting from DF
personsFromDisk.createOrReplaceTempView("PersonTable")
val result = spark.sql("select * from PersonsTable")

