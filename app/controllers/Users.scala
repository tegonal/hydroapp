package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.i18n._
import models.User

object Users extends Controller {

  val signupForm = Form[User](
    mapping(
      "email" -> email,
      "password" -> tuple(
        "main" -> nonEmptyText,
        "confirm" -> nonEmptyText).verifying(
          Messages("passwords.notmatching"), passwords => passwords._1 == passwords._2).
          transform({ _._1 }, { p: String => p -> p }),
      "firstName" -> optional(text),
      "lastName" -> optional(text))(User.apply)(User.unapply))

  def signup = Action { implicit request =>
    Ok(views.html.signup(signupForm))
  }

  def create = Action { implicit request =>
    signupForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.signup(formWithErrors)),
      user => models.Users.getByEmail(user.email).map { existing =>
        BadRequest(views.html.signup(signupForm))
      }.getOrElse {
        models.Users.create(user)
        Redirect(routes.Application.index()).withNewSession.flashing(
          "success" -> Messages("signup.success"))
      })
  }

}