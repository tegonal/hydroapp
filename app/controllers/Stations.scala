package controllers

import play.api._
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.ws.WS
import models._
import play.api.libs.json._
import play.api.i18n.Messages
import java.text.SimpleDateFormat
import org.joda.time.DateTime
import com.github.nscala_time.time.Imports._
import play.api.libs.ws.WS._

trait StationsController extends Controller {
  // injected station source
  this: StationSource =>

  val TEMP = "TEMPERATURE"
  val FLOW = "FLOW_M3_S"

  val historyTypes = List(TEMP, FLOW)

  def get(id: Int) = Action {
    Async {
      retrieveStation(id).map {
        case Some(station) => Ok(views.html.stations.station(station))
        case None => NotFound
      }
    }
  }

  def list(filter: String) = Action { request =>
    Async {
      retrieveStationList(filter).map { list =>
        val stats = request.session.get(Security.username).map { email =>
          val favs = models.Favourites.findByEmail(email)
          list map (s => FavMeasuringStation(s, favs exists (_.stationId == s.id)))
        }.getOrElse {
          list map (FavMeasuringStation(_))
        }
        val (favourites, stations) = stats partition (_.isFavourite)

        Ok(views.html.stations.list(favourites, stations))
      }
    }
  }

  def chart(id: Int, daysInPast: Int) = Action {
    Async {
      val to = DateTime.now
      val from = DateTime.now - daysInPast.days

      retrieveStationHistory(id, from, to).map { stations =>

        Ok(Json.obj("cols" -> columns(stations),
          "rows" -> rows(stations),
          "options" ->
            Json.obj("curveType" -> "function", "title" -> "and some time ago...",
              "hAxis" -> Json.obj("title" -> "Date & Time", "titleTextStyle" -> Json.obj("color" -> "#333")),
              "vAxes" -> (Json.obj() /: titles)(_ + _),
              "series" -> Json.obj("1" -> Json.obj("targetAxisIndex" -> "1")))))

      }
    }
  }

  def columns(stations: Seq[MeasuringStation]) = {
    Json.obj("id" -> "date", "label" -> Messages("timeofmeasurement"), "type" -> "datetime") +:
      (stations flatMap (_.measurements) map (_.measurementType) distinct).sortWith(_ > _).collect {
        case TEMP => Json.obj("id" -> "temperature", "label" -> Messages(TEMP), "type" -> "number")
        case FLOW => Json.obj("id" -> "flow", "label" -> Messages(FLOW), "type" -> "number")
      }
  }

  def rows(stations: Seq[MeasuringStation]) = {
    (stations map (_.measurements)).map { ms =>
      Json.obj("c" -> (
        Json.obj("v" -> toGoogleChartDate(ms map (_.date) max)) +:
        (ms filter (s => historyTypes.contains(s.measurementType)) sortWith (_.measurementType > _.measurementType)).map { m =>
          Json.obj("v" -> m.current)
        }))
    }
  }

  def titles = {
    (historyTypes zipWithIndex).map {
      case (title, i) => (i.toString, Json.obj("title" -> Messages(title + "_unit")))
    }
  }

  def toGoogleChartDate(date: java.util.Date): String =
    "Date(%d)".format(date.getTime())
}

object Stations extends StationsController with WsSource
