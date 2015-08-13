package models

import play.api.libs.json._

/**
 * Created by Diogo on 06/08/2015.
 */

case class Person(id: Long, name: String, age: Int)

object Person {
  implicit val personFormat = Json.format[Person]
}

/*
class Person {

}
*/