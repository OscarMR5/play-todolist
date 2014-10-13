package models

import anorm._
import anorm.SqlParser._
import scala.language.postfixOps
import play.api.db._
import play.api.Play.current

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
      SQL("select * from user where name = {name}").on('name -> name).as(user.singleOpt)
    }
  }
  
  
}