package controllers

import play.api._
import play.api.mvc._

import play.api.data._
import play.api.data.Forms._

import models.Task
import models.User

import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._

import java.util.Date
import java.text.SimpleDateFormat

object Application extends Controller {

  val fechaWrite = Writes.dateWrites("yyyy-MM-dd")
  val fechaRead = Reads.dateReads("yyyy-MM-dd")
  val fechaFormatter = new SimpleDateFormat("yyyy-MM-dd")
  
  implicit val taskWrites: Writes[Task] = (
    (JsPath \ "id").write[Long] and
    (JsPath \ "label").write[String] and
    (JsPath \ "owner").write[String] and
    (JsPath \ "fecha").writeNullable[Date](fechaWrite)
  )(unlift(Task.unapply))

  implicit val taskReads: Reads[Task] = (
    (JsPath \ "id").read[Long] and
    (JsPath \ "label").read[String] and
    (JsPath \ "owner").read[String] and
    (JsPath \ "fecha").readNullable[Date](fechaRead)
  )(Task.apply _)

   val taskForm = Form(
      "label" -> nonEmptyText
   )

  def index = Action {
   Redirect(routes.Application.tasksOLD)
    //Ok(views.html.index("Your new application is ready."))
    //Ok("Hello world)
  }
  
  //En feature 2 devuelve las tareas de guest
  def tasks = Action {
    val json = Json.toJson(Task.listUserTask("guest"))
    Ok(json)
  }
  
  //Funcion anterior que devuelve la vista web
  def tasksOLD = Action {
    Ok(views.html.index(Task.all(), taskForm))
  }

  def readTask(id: Long) = Action {
    val tarea = Task.getTask(id)
    if (tarea.isEmpty) {
      NotFound//("No existe la tarea con id:"+ id)
    } else {
      val json = Json.toJson(tarea.get)
      Ok(json)
    }
  }

  //Funcion de Feature2 listar tareas de un usuario.
  def readUserTasks(user: String) = Action {
    if (User.getUser(user).isEmpty) {
      NotFound
    } else {
      val json = Json.toJson(Task.listUserTask(user))
      Ok(json)
    }
  }

  //Funcion de Feature3 listar tareas de un usuario y fecha
  def readUserTasks(user: String, fechaIn: Option[String]) = Action {
    if (User.getUser(user).isEmpty) {
      NotFound
    } else {
      val fecha = fechaIn match{
        case Some(fechaIn) => Some(fechaFormatter.parse(fechaIn))
        case None => None
      }
      val json = Json.toJson(Task.listUserTask(user, fecha))
      Ok(json)
    }
  }

  //Funcion de Feature3 Listar las tareas de un usuario en la fecha actual
  def readUserTasksNow(user: String) = 
    readUserTasks(user, Some(fechaFormatter.format(new Date)))

  //En feature 2 crea tareas con owner guest
  def newTask = Action { implicit request =>
    taskForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(Task.all(), errors)),
      label => {
        val id = Task.create(label) //valor por defecto para owner es guest
        if (id.isEmpty) {
          InternalServerError
        } else {
          val json = Json.toJson(Task.getTask(id.get).get)
          Created(json)
        }
      })
  }

  //Funcion de Feature2 nueva tarea de un usuario a partir de JSON Task recibido comprabar el label
  def newUserTask(user: String) = Action(BodyParsers.parse.json) { request =>
    val taskResult = request.body.validate[Task]
    taskResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(errors)))
      },
      task => {
        if(User.getUser(user ).isEmpty){
          NotFound("Usuario no registrado")
        }
        else{
        Task.create(task.label, user)
        Ok(Json.obj("status" -> "OK", "message" -> ("Tarea: " + task.label + " guardada.")))
        }
      })
  }

  def deleteTask(id: Long) = Action {
    if (Task.delete(id) == 1)
      Ok
    else
      NotFound
  }

  //Funcion de Feature3 Borrar las tareas de un usuario de la fecha indicada
  def deleteTask(user: String, fechaIn: Option[String]) = Action {
    if (User.getUser(user).isEmpty) {
      NotFound
    } else {
      val fecha = fechaIn match{
        case Some(fechaIn) => Some(fechaFormatter.parse(fechaIn))
        case None => None //No se ha indicado la fecha
      }
      if(fecha == None)
        BadRequest //No se ha indicado la fecha
      else{
        val tareas = Task.listUserTask(user, fecha)
        tareas.foreach(x => Task.delete(x.id))//TODO todas las pruebas
        Ok
      }
    }
  }
  
  //Funcion antigua de borrado con interfaz web
  def deleteTaskOLD(id: Long) = Action {
    Task.delete(id)
    Redirect(routes.Application.tasks)
  }


}