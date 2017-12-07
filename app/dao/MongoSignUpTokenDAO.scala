package dao
import java.util.UUID
import javax.inject.Inject

import play.api.libs.json.Json
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json._
import reactivemongo.play.json.collection.JSONCollection
import tokens.SignUpToken

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class MongoSignUpTokenDAO @Inject()(reactiveMongoApi: ReactiveMongoApi) extends SignUpTokenDAO {

  val signupTokenCollection = reactiveMongoApi.database.map(_.collection[JSONCollection]("signup_tokens"))

  /**
    * DataBaseに保存されているSignUpTokenを取得する
    * @param tokenId 検索に必要なキー
    * @return
    */
  override def find(tokenId: UUID): Future[Option[SignUpToken]] = for {
    tokens <- signupTokenCollection
    signupToken <- tokens.find(Json.obj(
      "tokenID" -> tokenId
    )).one[SignUpToken]
  } yield signupToken

  /**
    * 引数で渡されたトークンを保存する
    * すでにトークンが保存されているなら update
    * トークンの新規作成なら add を呼び出す
    * @param token 保存対象のトークン
    * @return 保存されたトークン
    */
  override def save(token: SignUpToken): Future[SignUpToken] = {
    find(token.tokenID).flatMap{
      case None => add(token)
      case Some(signUpToken) => update(token)
    }
  }

  /**
    * トークンを新規追加する
    * @param token 新規追加するトークン
    * @return 追加されたトークン
    */
  override def add(token: SignUpToken): Future[SignUpToken] = for {
    tokens <- signupTokenCollection
    signupToken <- tokens.insert(token).map(_ => token)
  } yield signupToken

  /**
    * 変更のあったトークンの更新
    * @param token 更新するトークン
    * @return 更新されたトークン
    */
  override def update(token: SignUpToken): Future[SignUpToken] = for {
    tokens <- signupTokenCollection
    signUpToken <- tokens.update(Json.obj(
      "tokenID" -> token.tokenID
    ), Json.obj("$set" -> Json.obj("$" -> token))).map(_ => token)
  } yield signUpToken

  /**
    * 登録したトークンの削除を行う
    * @param tokenId 削除されるトークンのID
    * @return Unitなので戻り値はない
    */
  override def remove(tokenId: UUID): Future[Unit] = for {
    tokens <- signupTokenCollection
    signupToken <- tokens.remove(Json.obj("tokenID" -> tokenId))
  } yield signupToken
}
