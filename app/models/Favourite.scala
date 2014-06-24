package models

import play.api.Play.current
import play.api.db.slick.Profile
import play.api.db.slick.DB
import play.api.db.slick.Config.driver.simple._
import scala.slick.driver.ExtendedProfile
import org.joda.time.DateTime

case class Favourite(email: String, stationId: Int)

class Favourites(tag: Tag) extends Table[Favourite](tag, "FAVOURITES") {
  def email = column[String]("EMAIL")
  def stationId = column[Int]("STATION_ID")

  def * = (email, stationId) <> (Favourite.tupled, Favourite.unapply _)

  def user = foreignKey("FK_FAV_USER", email, Users.users)(_.email)

  def idx = index("IDX_FAV_USER_STATION", (email, stationId), unique = true)
}

object Favourites {
  val favourites = TableQuery[Favourites]

  def create(favourite: Favourite) = DB.withSession { implicit s: Session =>
    favourites.insert(favourite)
    favourite
  }

  def delete(favourite: Favourite) = DB.withSession { implicit s: Session =>
    favourites.filter(f => f.email === favourite.email && f.stationId === favourite.stationId).delete > 0
  }
  
  def find(favourite: Favourite) = DB.withSession { implicit s: Session =>
    favourites.filter(f => f.email === favourite.email && f.stationId === favourite.stationId).firstOption
  }

  def findByEmail(email: String) = DB.withSession { implicit s: Session =>
    favourites.filter(_.email === email).list
  }
}
