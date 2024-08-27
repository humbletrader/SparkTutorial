package com.github.sparktutorial

package object refdata {
  case class Person(id: Int, firstName: String, lastName: String, age: Int, gender: String)
  case class Employee(id: Int, firstName: String, lastName: String, department: String, salary: Double)
}
