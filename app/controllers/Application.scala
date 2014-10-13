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

object Application extends Controller {
 

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

  def newTask = Action { implicit request =>
    taskForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(Task.all(), errors)),
      label => {
        val id = Task.create(label)
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
  
  //Funcion antigua de borrado con interfaz web
  def deleteTaskOLD(id: Long) = Action {
    Task.delete(id)
    Redirect(routes.Application.tasks)
  }


}