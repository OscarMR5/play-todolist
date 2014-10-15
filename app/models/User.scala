package models

import anorm._
import anorm.SqlParser._
import scala.language.postfixOps
import play.api.db._
import play.api.Play.current
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class User(name: String)

object User {
  
  val user = {
    get[String]("name") map {
        case id => User(id)
      }
  }

  /**
   * Retrieve a user from the id.
   */
  def getUser(name: String): Option[User] = {
    DB.withConnection { implicit connection =>
      SQL("select * from taskuser where name = {name}").on('name -> name).as(user.singleOpt)
    }
  }
  
implicit val userWrites = new Writes[User] {
  def writes(user : User) = Json.obj(
    "name" -> user.name
  )
}
}