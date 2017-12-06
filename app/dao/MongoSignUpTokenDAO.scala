package dao
import java.util.UUID

import tokens.SignUpToken

import scala.concurrent.Future

class MongoSignUpTokenDAO extends SignUpTokenDAO {
  override def find(tokenId: UUID): Future[Option[SignUpToken]] = ???
  override def save(token: SignUpToken): Future[SignUpToken] = ???
  override def add(token: SignUpToken): Future[SignUpToken] = ???
  override def update(token: SignUpToken): Future[SignUpToken] = ???
  override def remove(tokenId: UUID): Future[Unit] = ???
}
