package dao

import javax.inject.Inject

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.persistence.daos.DelegableAuthInfoDAO
import play.modules.reactivemongo.json._
import play.api.libs.json.Json
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.DB
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

case class PersistentPasswordInfo(loginInfo: LoginInfo, authInfo: PasswordInfo)

class PasswordInfoDAO @Inject()(reactiveMongoApi: ReactiveMongoApi, ec:ExecutionContext) extends DelegableAuthInfoDAO[PasswordInfo] {

  implicit val passwordInfoFormat = Json.format[PasswordInfo]
  implicit val persistentPasswordInfoFormat = Json.format[PersistentPasswordInfo]

  val password = reactiveMongoApi.database.map(_.collection[JSONCollection]("events"))

  def collection: JSONCollection = ???

  def find(loginInfo: LoginInfo) = {

    val passwordInfo = for {
      event <- password
      passwordInfo <- event.find(Json.obj("loginInfo" -> loginInfo)).one[PersistentPasswordInfo]
    } yield passwordInfo

    passwordInfo.flatMap {
      case None =>
        Future.successful(Option.empty[PasswordInfo])
      case Some(persistentPasswordInfo) =>
        Future(Some(persistentPasswordInfo.authInfo))
    }
  }

  def add(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
    collection.insert(PersistentPasswordInfo(loginInfo, authInfo)).map(_ => authInfo)
  }

  def update(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
    Future.successful(authInfo)
  }

  def save(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
    find(loginInfo).flatMap {
      case Some(_) => update(loginInfo, authInfo)
      case None => add(loginInfo, authInfo)
    }
  }

  def remove(loginInfo: LoginInfo): Future[Unit] = {
    Future.successful("123".toString())
  }


}