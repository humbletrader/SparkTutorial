case class Person(firstName: String, lastName: String, age: Int, gender: String, salary: Double)
val personSeq = Seq.tabulate(100){idx => 
    Person(
        s"firstName $idx", 
        s"lastName $idx", 
        idx, 
        if(idx % 2 == 0) "M" else "F", 
        if(idx %2 ==0) 1000 else 2000
    )
}

//creating df from rdd
val personRdd = sc.parallelize(personSeq)
val personDf = personRdd.toDF()

//creating df from Seq
import spark.implicits._
val dfFromSeq = personSeq.toDF()

//writing to parquet
personDf.write.partitionBy("gender").parquet("/tmp/test/persons.parquet")

//reading from parquet
val personsFromDisk = spark.read.parquet("/tmp/test/personsp.parquet")


//selecting from DF
personsFromDisk.createOrReplaceTempView("PersonTable")
val result = spark.sql("select * from PersonsTable")

