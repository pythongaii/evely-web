package dao


import javax.inject.Inject

import com.mohiva.play.silhouette.api.LoginInfo

import play.api.libs.json.Json
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json._
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import model.event.Event


class EventDAO @Inject()(reactiveMongoApi: ReactiveMongoApi){

  val eventCollection = reactiveMongoApi.database.map(_.collection[JSONCollection]("event"))

  def find(eventId: Int):Future[Option[Event]] = for {
    events <- eventCollection
    event <- events.find(Json.obj("eventId" ->
      eventId)).one[Event]
  } yield event




}
