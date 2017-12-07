package dao

import javax.inject.Inject

import com.mohiva.play.silhouette.api.LoginInfo
import model.user.RegisteredUser
import play.api.libs.json.Json
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.Future

class MongoUserDAO @Inject()(reactiveMongoApi: ReactiveMongoApi) extends UserDAO {

  val userCollection = reactiveMongoApi.database.map(_.collection[JSONCollection]("users"))

  /**
    * loginInfo を元にユーザ情報をDBから取得する
    *
    * @param loginInfo ユーザを一意に識別する
    * @return 当該のユーザ情報 戻り値がからの場合もある
    */
  override def find(loginInfo: LoginInfo): Future[Option[RegisteredUser]] = for {
    users <- userCollection
    registerdUser <- users.find(Json.obj("loginInfo" -> loginInfo)).one[RegisteredUser]
  } yield registerdUser

  /**
    * userName を元にユーザ情報をDBから取得する
    *
    * @param userName ユーザを一意に識別する
    * @return 当該のユーザ情報 戻り値が空の場合もある
    */
  override def find(userName: String): Future[Option[RegisteredUser]] = for {
    users <- userCollection
    registerdUser <- users.find(Json.obj("userName" -> userName)).one[RegisteredUser]
  } yield registerdUser

  /**
    * 引数で渡されたRegisterUserオブジェクトを保存する
    * すでに保存されたユーザであれば、updateメソッド
    * 新規登録ユーザであれば addメソッド
    *
    * @param user 登録するユーザオブジェクト
    * @return 保存したユーザオブジェクト
    */
  override def save(user: RegisteredUser): Future[RegisteredUser] = {
    find(user.userName).flatMap {
      case None => add(user)
      case Some(registeredUser) => update(user)
    }
  }

  /**
    * 引数で渡されたユーザ情報をDBに新規追加する
    *
    * @param user 新規保存するユーザ情報
    * @return 保存したユーザ情報
    */
  override def add(user: RegisteredUser): Future[RegisteredUser] = for {
    users <- userCollection
    registeredUser <- users.insert(user).map(_ => user)
  } yield registeredUser

  /**
    * 引数で渡されたユーザ情報で保存された情報を更新する
    *
    * @param user 更新があったユーザ情報
    * @return 更新したユーザ情報
    */
  override def update(user: RegisteredUser): Future[RegisteredUser] = for {
    users <- userCollection
    registeredUser <- users.update(Json.obj(
      "userName" -> user.userName
    ), Json.obj("$set" -> Json.obj("$" -> user))).map(_ => user)
  } yield registeredUser

  /**
    * 登録したユーザ情報を削除する
    *
    * @param loginInfo ユーザを一意に識別する情報
    * @return Unitなので戻り値はない
    */
  override def remove(loginInfo: LoginInfo): Future[Unit] = for {
    users <- userCollection
    user <- users.remove(Json.obj("loginInfo" -> loginInfo))
  } yield user

  override def remove(userName: String): Future[Unit] = for {
    users <- userCollection
    user <- users.remove(Json.obj("userName" -> userName))
  } yield user
}

