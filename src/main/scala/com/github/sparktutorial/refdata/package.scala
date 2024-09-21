package com.github.sparktutorial

package object refdata {
  case class Person(id: Int, firstName: String, lastName: String, gender: String, strDateOfBirth: String)

  case class PersonWithAge(id: Int, firstName: String, lastName: String, age: Int, gender: String)

  case class Employee(id: Int, firstName: String, lastName: String, department: String, salary: Double)

  case class SecretInfo(secretKey: String, hiddenInfo: Map[String, String])

  case class Address(country: String, city: String, street: String, number: Int, secret: SecretInfo)

  case class PersonWithAddress(person: Person, address: Address)
}
