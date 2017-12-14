package controllers

import javax.inject.Inject

import dao.{AuthModule, PlainDAO}
import forms.CreateEventForm
import model.event.APIEvent
import play.api.cache.CacheApi
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.ws.WSResponse
import play.api.mvc._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class RegisteredHomeController @Inject()(cache: CacheApi,
                                         apiEventDAO: PlainDAO[String, APIEvent, WSResponse])(implicit val messagesApi: MessagesApi) extends AuthModule(cache) with I18nSupport{

  def home = withAuth { username => implicit request =>
    Future.successful(Ok(views.html.test.home()))
  }

  def index = withAuth { username => implicit request =>
    Future.successful(Ok(views.html.test.index(CreateEventForm.createEventForm)))
  }

  def bookmark = withAuth { username => implicit request =>
    Future.successful(Ok(views.html.test.bookmark()))
  }

  def notification = withAuth { username => implicit request =>
    Future.successful(Ok(views.html.test.notification()))
  }

  def nearBy = withAuth { username => implicit request =>
    Future.successful(Ok(views.html.test.nearby()))
  }

  def signout = withAuth { username => implicit request =>
    removeAuthInfo(request)
    Future.successful(Redirect(routes.GuestHomeController.index()).discardingCookies(DiscardingCookie(COOKIE_NAME)))
  }

  def create = withAuth { username => implicit request =>
    CreateEventForm.createEventForm.bindFromRequest().fold(
      errorForm => {
        errorForm
        Future.successful(Redirect(routes.GuestHomeController.index()))
      },
      eventData => {
        apiEventDAO.save(eventData, request).map {
          case res => Redirect(routes.RegisteredHomeController.index()).flashing(("created", "イベントを作成しました"))
          case _ => Redirect(routes.GuestHomeController.index())
        }
      }
    )}

}
