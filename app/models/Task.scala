package models

import anorm._
import anorm.SqlParser._
import scala.language.postfixOps
import play.api.db._
import play.api.Play.current
import play.api.libs.json._
import play.api.libs.functional.syntax._
import java.util.Date

//Fecha tipo Option con valor por defecto
case class Task(id: Long, label: String, owner: String, fecha: Option[Date] = None)

object Task {

  val task = {
  get[Long]("id") ~ 
  get[String]("label") ~ 
  get[String]("owner") ~ 
  get[Option[Date]]("fecha") map {
    case id~label~user~fecha => Task(id, label, user, fecha)
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
  
  //Listar tareas de un Usuario. Ahora tmb permite filtrar por fecha opcional
  def listUserTask(name: String, fecha: Option[Date] = None): List[Task] = DB.withConnection { implicit c =>
    fecha match {
      case Some(fecha) => SQL("select * from task where owner = {name} and fecha = {fecha}").on('name -> name, 'fecha -> fecha).as(task *)
      case None => SQL("select * from task where owner = {name}").on('name -> name).as(task *)
    }
  }

  def create(label: String, owner: String = "guest", fecha: Option[Date] = None): Option[Long] = {
    val id: Option[Long] = DB.withConnection { implicit c =>
      SQL("insert into task (label,owner, fecha) values ({label},{owner},{fecha})").on(
        'label -> label, 'owner -> owner, 'fecha -> fecha).executeInsert()
    }
    id
  }

  def delete(id: Long) = {
    DB.withConnection { implicit c =>
      SQL("delete from task where id = {id}").on(
        'id -> id).executeUpdate()
    }
  }
}