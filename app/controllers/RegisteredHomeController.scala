package controllers

import javax.inject.Inject

import dao.{AuthModule, PlainDAO}
import forms.{CreateEventData, CreateEventForm}
import play.api.cache.CacheApi
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.ws.WSResponse
import play.api.mvc._
import utils.ConfigProvider

import scala.concurrent.Future

class RegisteredHomeController @Inject()(cache: CacheApi,
                                         apiEventDAO: PlainDAO[CreateEventData, WSResponse],
                                         configProvider: ConfigProvider)(implicit val messagesApi: MessagesApi) extends AuthModule(cache, configProvider) with I18nSupport {

  def home = withAuth { username =>
    implicit request =>
      Future.successful(Ok(views.html.secured.home()))
  }

  def index = withAuth { username =>
    implicit request =>
      Future.successful(Ok(views.html.secured.index(CreateEventForm.createEventForm)(username)("name")))
  }

  def bookmark = withAuth { username =>
    implicit request =>
      Future.successful(Ok(views.html.secured.bookmark()))
  }

  def notification = withAuth { username =>
    implicit request =>
      Future.successful(Ok(views.html.secured.notification()))
  }

  def nearBy = withAuth { username =>
    implicit request =>
      Future.successful(Ok("nearby"))
  }

  def signout = withAuth { username =>
    implicit request =>
      removeAuthInfo(request)
      Future.successful(Redirect(routes.GuestHomeController.index()).discardingCookies(DiscardingCookie(configProvider.COOKIE_NAME)))
  }

}
