package example.json

import shapeless.{HNil, ::, HList, Lazy, Inl, Inr, :+:, CNil}
import shapeless.Witness
import shapeless.labelled.FieldType
import shapeless.LabelledGeneric
import shapeless.Coproduct

trait JsonEncoder[T] {
  def encode(input: T): JsValue
}

object JsonEncoder {
  def pure[T](f: T => JsValue): JsonEncoder[T] = new JsonEncoder[T] {
    override def encode(input: T): JsValue = f(input)
  }

  def encode[A](value: A)(implicit enc: JsonEncoder[A]): JsValue =
    enc.encode(value)

  implicit val intEnc: JsonEncoder[Int] = pure(JsNumber(_))
  implicit val doubleEnc: JsonEncoder[Double] = pure(JsNumber(_))
  implicit val stringEnc: JsonEncoder[String] = pure(JsString(_))
  implicit val booleanEnc: JsonEncoder[Boolean] = pure {
    case true  => JsTrue
    case false => JsFalse
  }

  implicit def arrayEnc[A](implicit enc: JsonEncoder[A]): JsonEncoder[List[A]] =
    pure(arr => JsArr(arr.map(enc.encode)))

  implicit def optionEnc[A](implicit enc: JsonEncoder[A]): JsonEncoder[Option[A]] =
    pure(_.map(enc.encode).getOrElse(JsNull))

  implicit def geneticEnc[A, B](
      implicit gen: LabelledGeneric.Aux[A, B],
      enc: Lazy[JsonObjEncoder[B]]
  ): JsonEncoder[A] =
    pure(value => enc.value.encode(gen.to(value)))
}

trait JsonObjEncoder[T] {
  def encode(input: T): JsObj
}

object JsonObjEncoder {
  def pure[T](f: T => JsObj) = new JsonObjEncoder[T] {
    override def encode(input: T) = f(input)
  }

  implicit val hnilEnc: JsonObjEncoder[HNil] = JsonObjEncoder.pure(_ => JsObj(Nil))

  implicit def hlistEnc[K <: Symbol, H, T <: HList](
      implicit
      witness: Witness.Aux[K],
      hEnc: Lazy[JsonEncoder[H]],
      tEnc: JsonObjEncoder[T]
  ): JsonObjEncoder[FieldType[K, H] :: T] = {

    val fieldName = witness.value.name
    pure { hlist =>
      val head = hEnc.value.encode(hlist.head)
      val tail = tEnc.encode(hlist.tail)
      JsObj((fieldName, head) :: tail.obj)
    }
  }

  implicit val cnilEnc: JsonObjEncoder[CNil] = JsonObjEncoder.pure(_ => JsObj(Nil))

  implicit def coproductEnc[K <: Symbol, H, T <: Coproduct](
      implicit
      witness: Witness.Aux[K],
      hEnc: Lazy[JsonEncoder[H]],
      tEnc: JsonObjEncoder[T]
  ): JsonObjEncoder[FieldType[K, H] :+: T] = {

    val typeName = witness.value.name

    pure {
      case Inl(h) => JsObj(List(typeName -> hEnc.value.encode(h)))
      case Inr(t) => tEnc.encode(t)
    }
  }
}
