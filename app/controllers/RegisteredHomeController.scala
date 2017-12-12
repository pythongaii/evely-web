package controllers

import javax.inject.Inject

import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._

import scala.concurrent.Future

class RegisteredHomeController @Inject()()(implicit val messagesApi: MessagesApi) extends Controller with I18nSupport{


  def home = Action.async { implicit request =>
    Future.successful(Ok(views.html.test.home())
  }

  def index = Action.async { implicit request =>
    Future.successful(Ok(views.html.test.index()))
  }

  def bookmark = Action.async { implicit request =>
    Future.successful(Ok(views.html.test.bookmark()))
  }

  def notification = Action.async { implicit request =>
    Future.successful(Ok(views.html.test.notification()))
  }

  def nearBy = Action.async { implicit request =>
    Future.successful(Ok(views.html.test.nearby()))
  }

}
