import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.libs.json._
import play.api.test._
import play.api.test.Helpers._

import models.Task
import models.User

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class ApplicationSpec extends Specification {
  def strToDate(str: String) = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(str)
  "Application" should {

    "send 404 on a bad request" in new WithApplication{
      route(FakeRequest(GET, "/boum")) must beNone
    }

    "rendirect to tasks page" in new WithApplication{
      val home = route(FakeRequest(GET, "/")).get

      status(home) must equalTo(SEE_OTHER)
      redirectLocation(home) must beSome.which(_ == "/tasksold")
    }
    //Siguiendo el orden de las funciones en Application.scala
    "GET /task devuelve las tareas de guest en JSON" in new WithApplication{
      val result = route(FakeRequest(GET,"/tasks")).get

      val tasks = contentAsJson(result).validate[Array[Task]]
      val Some(lista) = tasks.asOpt
      lista(0).owner must equalTo("guest")
      lista(1).owner must equalTo("guest")
      lista(4).owner must equalTo("guest")
    }

    "GET /task/:id devuelve la tarea con la id indicada" in new WithApplication{
      val result = route(FakeRequest(GET,"/tasks/5")).get
      val tasks = contentAsJson(result).validate[Task]
      val Some(task) = tasks.asOpt
      task.label must equalTo("tarea5")
    }

    "GET /:user/tasks?fecha= -> lista de tareas por user y fecha" in new WithApplication{
      val user = "oscarmarco"
      val fecha = "2014-11-27"
      val result = route(FakeRequest(GET,"/"+user+"/tasks?fecha="+fecha)).get
      
      val tasks = contentAsJson(result).validate[Array[Task]]
      val Some(lista) = tasks.asOpt
      lista(0).label must equalTo("tarea10")
    }

    "POST /:user/tasks recibe task JSON y crea la tarea para user" in new WithApplication{
      val user = "davidblanco"
      val fecha = "2014-11-27"
      val task = new Task(0,"tareaTest",user,Option(strToDate(fecha)))
      val result = route(FakeRequest(GET,"/"+user+"/tasks").withJsonBody(Json.toJson(task))).get
      
      status(result) must equalTo(OK)

      //Probar con el modelo que se ha creado
    }


  }



}
