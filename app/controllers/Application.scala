package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

object Application extends Controller {

   val taskForm = Form(
      "label" -> nonEmptyText
   )

  def index = Action {
   Redirect(routes.Application.tasks)
    //Ok(views.html.index("Your new application is ready."))
    //Ok("Hello world)
  }

  def tasks = TODO
  
  def newTask = TODO
  
  def deleteTask(id: Long) = TODO

}