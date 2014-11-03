package test

import org.specs2.mutable._  
import play.api.test._  
import play.api.test.Helpers._

import models.User
import models.Task

class ModelSpec extends Specification {

    def dateIs(date: java.util.Date, str: String) = new java.text.SimpleDateFormat("yyyy-MM-dd").format(date) == str  
    def strToDate(str: String) = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(str)

    "Models User" should {

        "Devolver los usuarios existentes" in {  
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
                val Some(user) = User.getUser("oscarmarco") 
                user.name must equalTo("oscarmarco")  
                val Some(user1) = User.getUser("pedrogarcia") 
                user1.name must equalTo("pedrogarcia")  
                val Some(user2) = User.getUser("davidblanco") 
                user2.name must equalTo("davidblanco")
                val Some(user3) = User.getUser("javiertolosa") 
                user3.name must equalTo("javiertolosa")
                val Some(user4) = User.getUser("jorgeruiz") 
                user4.name must equalTo("jorgeruiz")
                val Some(user5) = User.getUser("guest") 
                user5.name must equalTo("guest")
            }
        }

        "Devolver None si no existe" in {  
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
                val user = User.getUser("noexiste") 
                user must beNone
            }
        }

    }  

    "Models Task" should {

        "Devolver las tareas existentes por id" in {  
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
                val Some(task1) = Task.getTask(1)
                task1.id must equalTo(1)
                task1.label must equalTo("tarea1")
                task1.owner must equalTo("oscarmarco")
                val Some(task5) = Task.getTask(5)
                task5.id must equalTo(5)
                task5.label must equalTo("tarea5")
                val Some(task9) = Task.getTask(9)
                task9.id must equalTo(9)
                task9.label must equalTo("tarea9")
                val Some(task13) = Task.getTask(13)
                task13.id must equalTo(13)
                task13.label must equalTo("tarea13")
                //Devolver None si no se encuentra
                val taskn = Task.getTask(262) 
                taskn must beNone
            }
        }

        "Devolver una lista con todas las tareas" in {
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
                val lista = Task.all()
                val task1 = lista(0)
                task1.id must equalTo(1)
                task1.label must equalTo("tarea1")
                task1.owner must equalTo("oscarmarco")
                val task13 = lista(12)      
                task13.id must equalTo(13)
                task13.label must equalTo("tarea13")
                //Suma de todos los IDs
                var sum: Long = 0
                lista.foreach (sum += _.id)
                sum must equalTo(91)
            }
        }

        "Devolver lista de tareas de un usuario" in {  
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
                val lista = Task.listUserTask ("javiertolosa")
                lista(0).label must equalTo("tarea4")
                lista(2).label must equalTo("tarea12")
            }
        }
        "Devolver la lista de tareas de un usuario y una fecha" in {  
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
                val lista = Task.listUserTask ("javiertolosa",Option(strToDate("2014-11-05")))
                lista(0).label must equalTo("tarea9")
            }
        }

        "Crear una tarea y Borrar una tarea ++" in {  
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
                val id = Task.create("TareaTest", "oscarmarco", Option(strToDate("2014-11-05")))
                id must beSome
                val task = Task.getTask(id.get);
                task must beSome
                task.get.label must equalTo("TareaTest")
                task.get.owner must equalTo("oscarmarco")
                task.get.fecha must equalTo(Option(strToDate("2014-11-05")))

                //Borrar
                Task.delete(id.get)
                val lista1 = Task.listUserTask ("oscarmarco",Option(strToDate("2014-11-05")))
                lista1.isEmpty must beTrue
            }    
        }      
    }
}