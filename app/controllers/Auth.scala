package controllers

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.i18n._
import models._

object Auth extends Controller with ProvidesHeader {
  def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.index("", formWithErrors)),
      user => Redirect(routes.Application.index()).withSession(Security.username -> user._1))
  }

  def logout = Action { implicit request =>
    Redirect(routes.Application.index()).withNewSession.flashing(
      "success" -> Messages("logout.success"))
  }

  val loginForm = Form(
    tuple(
      "email" -> text,
      "password" -> text) verifying (Messages("login.invalid"), result => result match {
        case (email, password) => models.Users.authenticate(email, password).isDefined
      }))
}

trait Secured {

  def email(request: RequestHeader) = request.session.get(Security.username)

  def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.Application.index())

  def withAuth(f: => String => Request[AnyContent] => Result) = Security.Authenticated(email, onUnauthorized) { user =>
    Action(request => f(user)(request))
  }

  /**
   * Wrapping the withAuth method to fetch the user
   */
  def IsAuthenticated(f: User => Request[AnyContent] => Result) = withAuth { username =>
    implicit request =>
      models.Users.getByEmail(username).map { user =>
        f(user)(request)
      }.getOrElse(Results.Forbidden)
  }
}