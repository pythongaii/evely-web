package dao

import javax.inject.Inject

import play.api.libs.json.Json

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import play.modules.reactivemongo.ReactiveMongoApi

import play.modules.reactivemongo.json._
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * PasswordInfo と LoginInfo をまとめて永続化するためのケースクラス
  * @param loginInfo ユーザに紐づいた LoginInfo
  * @param authInfo LoginInfo に紐づいた PasswordImfo
  */
case class PersistentPasswordInfo(loginInfo: LoginInfo, authInfo: PasswordInfo)

object PersistentPasswordInfo {
  // PersitentPasswordInfo の JsonParser
  implicit val persistentPasswordInfoFormat = Json.format[PersistentPasswordInfo]
}


/**
  * MongoDBにアクセスして、ユーザの PasswordInfo のCRUD操作を行う実装クラス
  * @param reactiveMongoApi mongoDBにアクセスするため
  */
class MongoPasswordInfoDao @Inject() (reactiveMongoApi: ReactiveMongoApi) extends PasswordInfoDAO {

  // password コレクションの参照を持つ
  val passwords = reactiveMongoApi.database.map(_.collection[JSONCollection]("password"))

  /**
    * 認証用のPasswordInfoを取得する
    * @param loginInfo PasswordInfoに紐づいたユーザのLogin情報
    * @return ユーザの登録したパスワードのハッシュ化した情報
    */
  override def find(loginInfo: LoginInfo): Future[Option[PasswordInfo]] = ???

  /**
    * loginInfoとPasswoedInfoを関連付けて新規追加する
    * @param loginInfo PasswordInfoに紐づいたユーザのLogin情報
    * @param authInfo ユーザ入力したパスワードをハッシュ化した情報
    * @return 登録したPassworfInfo
    */
  override def add(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = ???

  /**
    * 登録したPasswordInfoを削除する
    * @param loginInfo 削除したいPassworInfoに紐づいたユーザのLoginInfo
    * @return Unit型なので無し
    */
  override def remove(loginInfo: LoginInfo): Future[Unit] = ???

  /**
    * 登録した LoginInfo と PassworInfo を保存する
    * find を使用して PasswordInfo の登録有無を確認する
    * 既に PasswordInfo が登録されていれば update を呼び出す
    * 新規追加の場合は add を呼び出す
    * @param loginInfo 保存したい LoginInfo
    * @param authInfo 保存したい LoginInfo に紐づいた PasswordInfo
    * @return 保存した PasswordInfo
    */
  override def save(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = ???

  /**
    * 登録された PasswordInfo の情報を更新する
    * パスワードの再発行で使用される
    * @param loginInfo 再発行を求めたユーザの LoginInfo
    * @param authInfo 新しい PasswordInfo
    * @return 更新した PasswordInfo
    */
  override def update(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = ???

}
