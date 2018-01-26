package controllers

import javax.inject.Inject

import dao.PlainDAO
import forms.{CreateEventData, CreateEventForm}
import model.event.Event
import play.api.cache.CacheApi
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.Json
import play.api.libs.ws.WSResponse
import play.api.mvc._
import utils.{AuthModule, ConfigProvider}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class RegisteredHomeController @Inject()(cache: CacheApi,
                                         apiEventDAO: PlainDAO[CreateEventData, WSResponse],
                                         configProvider: ConfigProvider)(implicit val messagesApi: MessagesApi) extends AuthModule(cache, configProvider) with I18nSupport {

  def home = withAuth { username =>
    implicit request =>
      Future.successful(Ok(views.html.secured.home(username)))
  }

  def index = withAuth { username =>
    implicit request =>
      val query = Seq(("url", configProvider.EVENT_URL), ("keyword" -> "Git"), ("limit" -> "9"), ("offset" -> "0"))
      val collection = apiEventDAO.find(request,query: _*) map {
        case res => {
          Json.parse(res.get.body).validate[List[Event]].get
        }
      }
      collection.map(collection => Ok(views.html.secured.index(collection)(username)))
  }

  def bookmark = withAuth { username =>
    implicit request =>
      Future.successful(Ok(views.html.secured.bookmark(username)("name")))
  }

  def notification = withAuth { username =>
    implicit request =>
      Future.successful(Ok(views.html.secured.notification(username)("name")))
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
