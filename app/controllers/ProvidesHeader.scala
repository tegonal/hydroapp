package controllers

import play.api.mvc._

trait ProvidesHeader {

  implicit def header[A](implicit request: Request[A]): Header = {
    val email = request.session.get(Security.username)
    Header(email)
  }
}

case class Header(user: Option[String])