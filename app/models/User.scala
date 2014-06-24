package models

import play.api.Play.current
import play.api.db.slick.Profile
import play.api.db.slick.DB
import play.api.db.slick.Config.driver.simple._
import scala.slick.driver.ExtendedProfile
import org.joda.time.DateTime

case class User(email: String, password: String, firstName: Option[String], lastName: Option[String])

class Users(tag: Tag) extends Table[User](tag, "USERS") {
  def email = column[String]("EMAIL", O.PrimaryKey)
  def password = column[String]("PASSWORD", O.NotNull)
  def firstName = column[String]("FIRSTNAME", O.Nullable)
  def lastName = column[String]("LASTNAME", O.Nullable)

  def * = (email, password, firstName.?, lastName.?) <> (User.tupled, User.unapply _)
}

object Users {
  val users = TableQuery[Users]

  def authenticate(email: String, password: String) = DB.withSession { implicit s: Session =>
    users.filter(u => u.email === email && u.password === password).firstOption
  }

  def getByEmail(email: String) = DB.withSession { implicit s: Session =>
    users.filter(_.email === email).firstOption
  }

  def create(user: User) = DB.withSession { implicit s: Session =>
    users.insert(user)
    user
  }
}