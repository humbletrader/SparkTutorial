package com.github.sparktutorial.refdata

import org.apache.spark.sql.types.DateType

import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object Generator {

  def createPersons(): Seq[Person] = {
    Seq(
      Person(1, "John", "Doe",  "M",  "1980-12-31"),
      Person(2, "Jane", "Doe",  "F",  "1989-12-31"),
      Person(3, "Tom", "Smith",  "M", "1976-12-31"),
      Person(4, "Jill", "Johnson", "F", "1990-12-31"),
      Person(5, "Bill", "Doe",  "M",    "1999-12-31"),
      Person(6, "Jill", "Smith", "F",   "1956-12-31"),
      Person(7, "Tom", "Johnson", "M",  "2000-12-31"),
      Person(8, "John", "Smith",  "M",  "1980-12-31"),
      Person(9, "Jane", "Johnson", "F", "1980-12-31"),
      Person(10, "Bill", "Smith", "M",  "1980-12-31"),
      Person(100, "Child", "Unemployed", "M",  "2020-12-31") //this is not in the employees table
    )
  }

  def createEmployees(): Seq[Employee] = {
    Seq(
      Employee(1, "John", "Doe", "Engineering", 1000.0),
      Employee(2, "Jane", "Doe", "Engineering", 1200.0),
      Employee(3, "Tom", "Smith", "Sales", 1300.0),
      Employee(4, "Jill", "Johnson", "Marketing", 1400.0),
      Employee(5, "Bill", "Doe", "Engineering", 1500.0),
      Employee(6, "Jill", "Smith", "Sales", 1600.0),
      Employee(7, "Tom", "Johnson", "Marketing", 1700.0),
      Employee(8, "John", "Smith", "Engineering", 1800.0),
      Employee(9, "Jane", "Johnson", "Sales", 1900.0),
      Employee(10, "Bill", "Smith", "Marketing", 2000.0),
      Employee(11, "NoPerson", "Record", "Marketing", 2000.0) //<--- this is not in the Person data

    )
  }

  def computeAge(birthDateAsString: String): Int = {
    import java.time.LocalDate

    val now = LocalDate.now()
    val birthDate = LocalDate.parse(birthDateAsString, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    ChronoUnit.YEARS.between(birthDate, now).toInt
  }



}
