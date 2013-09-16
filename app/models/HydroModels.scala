package models

import java.util.Date
import play.api.libs.json._

case class Measurement(
  date: Date,
  current: Double,
  measurementType: String)

object Measurement {
  implicit val measurementFormat = Json.format[Measurement]
}

case class MeasuringStation(measuringStationId: Int, name: String, measurements: Seq[Measurement])

object MeasuringStation {
  implicit val measuringStationFormat = Json.format[MeasuringStation]
}

case class SimpleMeasuringStation(id: Int, name: String)

case class FavMeasuringStation(station: SimpleMeasuringStation, isFavourite: Boolean = false)

object SimpleMeasuringStation {
  implicit val simpleMeasuringStationFormat = Json.format[SimpleMeasuringStation]
}