package models

import anorm._
import anorm.SqlParser._
import scala.language.postfixOps
import play.api.db._
import play.api.Play.current
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Task(id: Long, label: String, owner: String)

object Task {

  val task = {
    get[Long]("id") ~
      get[String]("label")~
      get[String] ("owner")map {
        case id ~ label ~ owner => Task(id, label, owner)
      }
  }

  /**
   * Retrieve a task from the id.
   */
  def getTask(id: Long): Option[Task] = {
    DB.withConnection { implicit connection =>
      SQL("select * from task where id = {id}").on('id -> id).as(task.singleOpt)
    }
  }

  def all(): List[Task] = DB.withConnection { implicit c =>
    SQL("select * from task").as(task *)
  }
  
  def listUserTask(name: String): List[Task] = DB.withConnection { implicit c =>
    SQL("select * from task where owner = {name}").on('name -> name).as(task *)
  }

  def create(label: String, owner: String = "guest"): Option[Long] = {
    val id: Option[Long] = DB.withConnection { implicit c =>
      SQL("insert into task (label,owner) values ({label},{owner})").on(
        'label -> label, 'owner -> owner).executeInsert()
    }
    id
  }

  def delete(id: Long) = {
    DB.withConnection { implicit c =>
      SQL("delete from task where id = {id}").on(
        'id -> id).executeUpdate()
    }
  }
  
  implicit val taskWrites: Writes[Task] = (
  (JsPath \ "id").write[Long] and
  (JsPath \ "label").write[String] and
  (JsPath \ "owner").write[String])(unlift(Task.unapply))
}