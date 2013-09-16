package controllers

import play.api._
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.ws.WS
import play.mvc.Http
import org.omg.CosNaming.NamingContextPackage.NotFound
import models._
import play.api.libs.json._

object Application extends Controller with ProvidesHeader {

  def index(filter: String) = Action { implicit request =>
    Ok(views.html.index(filter, Auth.loginForm))
  }

  def javascriptRoutes = Action { implicit request =>
    import routes.javascript._
    Ok(
      Routes.javascriptRouter("jsRoutes")(
        Stations.list, Favourites.toggle)).as("text/javascript")
  }
}