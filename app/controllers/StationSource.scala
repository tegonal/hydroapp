package controllers

import play.api.libs.ws.WS
import play.api._
import play.api.mvc._
import play.api.mvc.Results._
import play.api.http.Status._
import models._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.Play.current
import scala.util.Success
import scala.util.Failure
import scala.concurrent.Future
import scala.util.Success
import scala.util.Failure
import org.joda.time.DateTime

trait StationSource {
  def retrieveStation(id: Int): Future[Option[MeasuringStation]]

  def retrieveStationList(filter: String): Future[Seq[SimpleMeasuringStation]]

  def retrieveStationHistory(id: Int, from: DateTime, to: DateTime): Future[Seq[MeasuringStation]]
}

trait WsSource extends StationSource {

  val baseWsUrl = Play.current.configuration.getString("hydro.ws.base").get

  override def retrieveStation(id: Int) =
    WS.url(baseWsUrl + "/stations/" + id).get().map { response =>
      response.status match {
        case OK => Some(response.json.as[MeasuringStation])
        case _ => None
      }
    }

  override def retrieveStationList(filter: String) =
    WS.url(baseWsUrl + "/stationList").withQueryString("filter" -> filter).get().map { response =>
      response.status match {
        case OK => response.json.as[Seq[SimpleMeasuringStation]]
        case _ => Nil
      }
    }

  override def retrieveStationHistory(id: Int, from: DateTime, to: DateTime) =
    WS.url(baseWsUrl + "/stations/" + id + "/history?from=" + from.getMillis + "&to=" + to.getMillis).get().map { response =>
      response.status match {
        case OK => response.json.as[Seq[MeasuringStation]]
        case _ => Nil
      }
    }

}
