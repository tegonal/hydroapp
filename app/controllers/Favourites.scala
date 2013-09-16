package controllers

import models._
import play.api.data.Forms._
import play.api.mvc._

object Favourites extends Controller with ProvidesHeader with Secured {

  def toggle(stationId: Int) = IsAuthenticated { user =>
    request =>
      models.Favourites.find(Favourite(user.email, stationId)).map {
        models.Favourites.delete
      }.getOrElse {
        models.Favourites.create(Favourite(user.email, stationId))
      }
      Ok(views.html.favourites())
  }

}
