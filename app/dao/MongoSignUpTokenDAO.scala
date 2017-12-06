package dao
import java.util.UUID
import javax.inject.Inject

import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.play.json.collection.JSONCollection
import tokens.SignUpToken

import scala.concurrent.Future

class MongoSignUpTokenDAO @Inject()(reactiveMongoApi: ReactiveMongoApi) extends SignUpTokenDAO {

  val collection = reactiveMongoApi.database.map(_.collection[JSONCollection])

  override def find(tokenId: UUID): Future[Option[SignUpToken]] = ???
  override def save(token: SignUpToken): Future[SignUpToken] = ???
  override def add(token: SignUpToken): Future[SignUpToken] = ???
  override def update(token: SignUpToken): Future[SignUpToken] = ???
  override def remove(tokenId: UUID): Future[Unit] = ???
}
