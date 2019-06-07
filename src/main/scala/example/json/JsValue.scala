package example.json

sealed trait JsValue

final case class JsObj(obj: List[(String, JsValue)]) extends JsValue
final case class JsArr(arr: List[JsValue]) extends JsValue

final case class JsString(str: String) extends JsValue
final case class JsNumber(num: Double) extends JsValue

sealed trait JsBoolean extends JsValue
case object JsTrue extends JsBoolean
case object JsFalse extends JsBoolean

case object JsNull extends JsValue
