package controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.api.Silhouette
import modules.CookieEnv
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.Json
import play.api.libs.ws.{WSClient, WSRequest}
import play.api.mvc._


import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

//
class TopController @Inject()(ws: WSClient, silhouette: Silhouette[CookieEnv])(implicit val messagesApi: MessagesApi) extends Controller with I18nSupport {

//  val eventList = {
//    val request: WSRequest = ws.url("http://160.16.140.145:8888/api/v1/events?limit=10&offset=0")
//    val futureres = request.get()
//    val events = futureres.map{
//      case res => res.body
//    }
//
//    Json.parse(events.value.get.get)
//  }

}
