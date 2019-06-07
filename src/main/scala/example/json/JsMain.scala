package example.json

import shapeless.::
import shapeless.HNil

object JsMain extends App {

  println("=" * 60)

  println("\n=========== Primitives ===========")
  println(JsonEncoder.encode("Pritam"))
  println(JsonEncoder.encode(true))
  println(JsonEncoder.encode(10))
  println(JsonEncoder.encode(List("Pritam", "Kadam")))
  println(JsonEncoder.encode(List(("name", "Pritam"), ("age", "29"))))

  println("\n=========== Product Types ===========")
  case class Person(name: String, age: Int)
  val p = Person("Bob", 55)

  println(JsonEncoder.encode(p))

  println("\n=========== CoProduct Types ===========")
  sealed trait Shape
  case class Rectangle(width: Int, height: Int) extends Shape
  case class Circle(readius: Int) extends Shape

  val rect: Shape = Rectangle(10, 20)
  val circle: Shape = Circle(10)

  println(JsonEncoder.encode(rect))
  println(JsonEncoder.encode(circle))

  println("=" * 60)
}
