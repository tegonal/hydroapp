package test

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import models._
import play.api.mvc._

class FavouritesSpec extends Specification {

  "Favourites controller" should {

    val email = "test@example.com"

    "add Favourite if it wasn't present before" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        Users.create(User(email, "password", None, None))

        val Some(result) = route(FakeRequest(GET, "/stations/42/toggle").withSession(Security.username -> email))

        status(result) must equalTo(OK)
        models.Favourites.findByEmail(email) must containAllOf(Seq(Favourite(email, 42)))
      }
    }

    "remove Favourite if it existed already" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        Users.create(User(email, "password", None, None))
        Favourites.create(Favourite(email, 42))

        val Some(result) = route(FakeRequest(GET, "/stations/42/toggle").withSession(Security.username -> email))

        status(result) must equalTo(OK)
        models.Favourites.findByEmail(email) must beEmpty
      }
    }
  }
}