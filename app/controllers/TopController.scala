package controllers

import javax.inject.Inject

import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._


import scala.concurrent.Future

//
class TopController @Inject()(implicit val messagesApi: MessagesApi) extends Controller with I18nSupport {

  def index() = Action.async {
    Future.successful(Ok(views.html.top(List("event1", "event2", "event3", "event4"))))
  }

  def Home = Action.async(Future.successful(Ok(views.html.test.home())))

  def Index = Action.async(Future.successful(Ok(views.html.test.index())))

  def Bookmark = Action.async(Future.successful(Ok(views.html.test.bookmark())))

  def Notification = Action.async(Future.successful(Ok(views.html.test.notification())))

  def Nearby = Action.async(Future.successful(Ok(views.html.test.nearby())))

}
