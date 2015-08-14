package models

import play.api.libs.json._

/**
 * Created by Diogo on 06/08/2015.
 */

case class Person(id: Long, firstname: String, lastname: String, email: String)

object Person {
  implicit val personFormat = Json.format[Person]
}

/*
class Person {

}
*/