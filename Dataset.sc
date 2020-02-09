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

val personsDS = spark.createDataset(personSeq)

//show top 20 persons
personsDS.show

//showing the schema behind this DS
personsDS.schema

//mapping
case class BetterPerson(firstName: String, lastName: String, middleName: String, age: Int, gender: String, salary: Double)
val betterPersonsDS = personsDS.map{case Person(first, last, age, gender, salary) => BetterPerson(first, last, "", age, gender, salary)}

betterPersonsDS.show

