package com.github.sparktutorial.refdata

object Generator {

  def createPersons(): Seq[Person] = {
    Seq(
      Person(1, "John", "Doe", 25, "M"),
      Person(2, "Jane", "Doe", 22, "F"),
      Person(3, "Tom", "Smith", 30, "M"),
      Person(4, "Jill", "Johnson", 28, "F"),
      Person(5, "Bill", "Doe", 27, "M"),
      Person(6, "Jill", "Smith", 29, "F"),
      Person(7, "Tom", "Johnson", 31, "M"),
      Person(8, "John", "Smith", 26, "M"),
      Person(9, "Jane", "Johnson", 23, "F"),
      Person(10, "Bill", "Smith", 32, "M")
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
      Employee(10, "Bill", "Smith", "Marketing", 2000.0)
    )
  }

}
