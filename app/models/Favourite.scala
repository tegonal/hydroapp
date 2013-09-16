package models

import play.api.Play.current
import play.api.db.slick.Profile
import play.api.db.slick.DB
import play.api.db.slick.Config.driver.simple._
import scala.slick.driver.ExtendedProfile
import org.joda.time.DateTime

case class Favourite(email: String, stationId: Int)

object Favourites extends Table[Favourite]("FAVOURITES") {
  def email = column[String]("EMAIL")
  def stationId = column[Int]("STATION_ID")

  def * = email ~ stationId <> (Favourite, Favourite.unapply _)

  def user = foreignKey("FK_FAV_USER", email, Users)(_.email)

  def idx = index("IDX_FAV_USER_STATION", (email, stationId), unique = true)

  def create(favourite: Favourite) = DB.withSession { implicit s: Session =>
    Favourites.insert(favourite)
    favourite
  }

  def delete(favourite: Favourite) = DB.withSession { implicit s: Session =>
    Favourites.filter(f => f.email === favourite.email && f.stationId === favourite.stationId).delete > 0
  }
  
  def find(favourite: Favourite) = DB.withSession { implicit s: Session =>
    Query(Favourites).filter(f => f.email === favourite.email && f.stationId === favourite.stationId).firstOption
  }

  def findByEmail(email: String) = DB.withSession { implicit s: Session =>
    Query(Favourites).filter(_.email === email).list
  }
}