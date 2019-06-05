package examples
import shapeless.{HList, ::, HNil}

object Main extends App {

  case class Person(fname: String, lname: String, age: Int)
  case class Employee(fname: String, lname: String, id: Int)

  val personCsv = CsvEncoder[Person].encode(Person("Pritam", "Kadam", 29))
  val employeeCsv = CsvEncoder[Employee].encode(Employee("Jack", "Bauer", 1827))

  println("=" * 15)
  println(personCsv.mkString(","))
  println(employeeCsv.mkString(","))
  println("=" * 15)
}
