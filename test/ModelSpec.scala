package test

import org.specs2.mutable._  
import play.api.test._  
import play.api.test.Helpers._

import models.User

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

        "Devolver las tareas existentes" in {  
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
                                val user = User.getUser("noexiste") 
                user must beNone
            }
        }
    }
}