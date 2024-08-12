// run this is spark shell
case class Person(firstName: String,
                  lastName: String,
                  age: Int,
                  gender: String
                 )

val personSeq = Seq.tabulate(100){idx => 
    Person(
        s"fn $idx",
        s"ln $idx",
        idx, 
        if(idx % 2 == 0) "M" else "F"
    )
}

val personsDS = spark.createDataset(personSeq)

//show top 20 persons
personsDS.show

//showing the schema behind this DS
personsDS.schema

case class Employee(firstName: String,
                    lastName: String,
                    age: Int,
                    gender: String,
                    salary: Double)

//dataset operations: filtering and mapping
val employeesDS = personsDS.filter(_.age > 18)
  .map{case Person(first, last, age, gender) =>
    Employee(first, last, age, gender, 100.1 * age)
  }
employeesDS.show


//dataset operations: select/where
employeesDS
  .select($"firstName")
  .where($"salary" > 1300)

//dataset operations: aggregations

//dataset operations: join two datasets

//dataset operations: repartitioning
employeesDS.repartition

//dataset operations: to rdd

//dataset operations: to dataframe

//dataset operations: read/write on disk / cloud
employeesDS.write.parquet("/tmp/test/employees.parquet")
employeesDS.write.csv("/tmp/test/employees.csv")
employeesDS.write.partitionBy("gender").parquet("/tmp/test/employees.parquet")
val employeesFromDisk = spark.read.parquet("/tmp/test/employees.parquet")

//dataset optimizations
// - catalyst optimizer
// - project tungsten




