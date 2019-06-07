package examples.csv

import shapeless.Generic
import shapeless.{HList, ::, HNil}

trait CsvEncoder[T] {
  def encode(in: T): List[String]
}

object CsvEncoder {

  def apply[T](implicit ev: CsvEncoder[T]) = ev

  def createEncoder[T](func: T => List[String]) = new CsvEncoder[T] {
    override def encode(in: T): List[String] = func(in)
  }

  implicit val hnilEncoder: CsvEncoder[HNil] = createEncoder(_ => Nil)

  implicit def hlistEncoder[H, T <: HList](
      implicit hEncoder: CsvEncoder[H],
      tEncoder: CsvEncoder[T]
  ): CsvEncoder[H :: T] = createEncoder {
    case h :: t => hEncoder.encode(h) ++ tEncoder.encode(t)
  }

  implicit def genericEncoder[A, R](
      implicit
      gen: Generic.Aux[A, R],
      enc: CsvEncoder[R]
  ): CsvEncoder[A] = createEncoder(a => enc.encode(gen.to(a)))

  implicit val intEncoder: CsvEncoder[Int] =
    createEncoder[Int](i => List(i.toString))

  implicit val stringEncoder: CsvEncoder[String] =
    createEncoder[String](List(_))

}
