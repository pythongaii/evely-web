package controllers

import javax.inject.Inject

import dao.{AuthModule, PlainDAO}
import forms.{CreateEventData, CreateEventForm, SearchEventForm}
import model.event.{APIEvent, Event}
import play.api.cache.CacheApi
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.Json
import play.api.libs.ws.WSResponse
import play.api.mvc._
import utils.ConfigProvider

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class RegisteredHomeController @Inject()(cache: CacheApi,
                                         apiEventDAO: PlainDAO[CreateEventData, WSResponse],
                                         configProvider: ConfigProvider)(implicit val messagesApi: MessagesApi) extends AuthModule(cache, configProvider) with I18nSupport {

  def home = withAuth { username =>
    implicit request =>
      Future.successful(Ok(views.html.secured.home()))
  }

  def index = withAuth { username =>
    implicit request =>
      Future.successful(Ok(views.html.secured.index(CreateEventForm.createEventForm)("id")("name")))
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
      Future.successful(Ok(views.html.secured.nearby()))
  }

  def signout = withAuth { username =>
    implicit request =>
      removeAuthInfo(request)
      Future.successful(Redirect(routes.GuestHomeController.index()).discardingCookies(DiscardingCookie(configProvider.COOKIE_NAME)))
  }

  def create = withAuth { username =>
    implicit request =>
      CreateEventForm.createEventForm.bindFromRequest().fold(
        errorForm => {
          Future.successful(Redirect(routes.GuestHomeController.index()))
        },
        eventData => {
          apiEventDAO.save(eventData, request).map {
            case res => Redirect(routes.RegisteredHomeController.index())
            case _ => Redirect(routes.GuestHomeController.index())
          }
        }
      )
  }

  def find = Action.async { implicit request =>
    SearchEventForm.form.bindFromRequest().forField("keyword")(
      field => {
        val collection = apiEventDAO.find(field.value.get) map {
          case res => {
            Json.parse(res.get.body).validate[List[Event]].get
          }
        }
        collection.map(collection => Ok(views.html.search_map_layout(collection, field.value.get)))
      }
    )
  }

}
