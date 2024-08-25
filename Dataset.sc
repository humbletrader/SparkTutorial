// run this is spark shell
case class Person( id : Int,
                   firstName: String,
                   lastName: String,
                   age: Int,
                   gender: String
                 )

val personSeq = Seq.tabulate(100){idx => 
    Person(
        idx,
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

case class Employee(  id: Int,
                      firstName: String,
                      lastName: String,
                      age: Int,
                      gender: String,
                      salary: Double)

//dataset operations: filtering and mapping
val employeesDS = personsDS.filter(_.age > 18)
  .map{case Person(id, first, last, age, gender) =>
    Employee(id, first, last, age, gender, 100.1 * age)
  }
employeesDS.show


//dataset operations: select/where
employeesDS
  .select($"firstName")
  .where($"salary" > 1300)

//dataset operations: aggregations

//dataset operations: join two datasets
personsDS.join(employeesDS, personsDS("id") === employeesDS("id"), "inner").show

personsDS.join(employeesDS, personsDS("id") === employeesDS("id"), "outer").show
personsDS.join(employeesDS, personsDS("id") === employeesDS("id"), "leftouter").show

personsDS.join(employeesDS, personsDS("id") === employeesDS("id"), "rigthouter").show

personsDS.join(employeesDS, personsDS("id") === employeesDS("id"), "fullouter").show

personsDS.join(employeesDS, personsDS("id") === employeesDS("id"), "semi").show
personsDS.join(employeesDS, personsDS("id") === employeesDS("id"), "leftsemi").show

personsDS.join(employeesDS, personsDS("id") === employeesDS("id"), "anti").show
personsDS.join(employeesDS, personsDS("id") === employeesDS("id"), "leftanti").show

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




