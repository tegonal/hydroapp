package test

import models._
import controllers.{ StationSource, StationsController }
import scala.concurrent.Future
import org.joda.time.DateTime

/**
 * Mock controller with expected return values as parameters
 */
case class MockStationsController(
  station: Option[MeasuringStation],
  list: Seq[SimpleMeasuringStation],
  history: Seq[MeasuringStation]) extends StationsController with TestStationSource

/**
 * Test source which returns always a successful future result
 */
trait TestStationSource extends StationSource {

  def station: Option[MeasuringStation]

  def list: Seq[SimpleMeasuringStation]

  def history: Seq[MeasuringStation]

  def retrieveStation(id: Int): Future[Option[MeasuringStation]] =
    Future.successful(station)

  def retrieveStationList(filter: String): Future[Seq[SimpleMeasuringStation]] =
    Future.successful(list)

  def retrieveStationHistory(id: Int, from: DateTime, to: DateTime): Future[Seq[MeasuringStation]] =
    Future.successful(history)
}