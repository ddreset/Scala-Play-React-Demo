package models

import play.api.libs.json._

case class Club(id: Option[Long], name: String, abbr: String)

case class Member(id: Option[Long], name: String, clubId: Option[Long])

// JSON serializer
object Club {  implicit val clubFormat = Json.format[Club] }

object Member {  implicit val memberFormat = Json.format[Member] }