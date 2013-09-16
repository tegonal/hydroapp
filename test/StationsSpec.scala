package test

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.ws.WS._
import play.api.libs.json._
import controllers.StationsController
import controllers.StationSource
import controllers.StationSource
import controllers.StationsController
import controllers.StationsController
import controllers.StationSource
import scala.concurrent.Future
import models._
import org.joda.time.DateTime
import controllers.StationsController

class StationsSpec extends Specification {

  "Stations controller" should {
    val measurements = Seq(Measurement(new java.util.Date, 18.5, "TEMPERATURE"))

    val testStationsController = MockStationsController(
      Some(MeasuringStation(2135, "Bern", measurements)),
      Seq(SimpleMeasuringStation(2135, "Bern")),
      Seq(MeasuringStation(2135, "Bern", measurements)))

    val controllerWithEmptySource = MockStationsController(None, Nil, Nil)

    "respond to the get Action with a MeasuringStation as html" in {
      running(FakeApplication()) {
        val result = testStationsController.get(2135)(FakeRequest())

        status(result) must equalTo(OK)
        contentType(result) must beSome("text/html")
        contentAsString(result) must contain("Bern")
      }
    }

    "respond to the list Action with a list of SimpleMeasuringStations as html" in {
      running(FakeApplication()) {
        val result = testStationsController.list("")(FakeRequest())

        status(result) must equalTo(OK)
        contentType(result) must beSome("text/html")
        contentAsString(result) must contain("Bern")
      }
    }

    "respond to the chart Action with Json data" in {
      running(FakeApplication()) {
        val result = testStationsController.chart(2135, 30)(FakeRequest())

        status(result) must equalTo(OK)
        contentType(result) must beSome("application/json")
        contentAsString(result) must contain("18.5")
      }
    }

    "respond to the get Action with NotFound for unmatched id" in {
      running(FakeApplication()) {
        val result = controllerWithEmptySource.get(42)(FakeRequest())

        status(result) must equalTo(NOT_FOUND)
      }
    }
  }
}
