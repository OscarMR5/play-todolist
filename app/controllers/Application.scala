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
  
  //Nueva funcion que funciona a traves de API REST
  def tasks = Action {
    val json = Json.toJson(Task.all())
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
    
    val json = Json.toJson(Task.all())
    Ok(json)
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