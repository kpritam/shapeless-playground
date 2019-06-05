package examples
import shapeless.{HList, ::, HNil}

object Main extends App {

  case class Person(fname: String, lname: String, age: Int)
  case class Employee(name: String, id: Int)

  val personCsv = CsvEncoder[Person].encode(Person("Pritam", "Kadam", 29))
  val employeeCsv = CsvEncoder[Employee].encode(Employee("Jack", 1827))

  println("=" * 15)
  println(personCsv.mkString(","))
  println(employeeCsv.mkString(","))
  println("=" * 15)
}
