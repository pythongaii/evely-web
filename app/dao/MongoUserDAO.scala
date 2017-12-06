package dao
import com.mohiva.play.silhouette.api.LoginInfo
import model.user.RegisteredUser

import scala.concurrent.Future

class MongoUserDAO extends UserDAO {
  override def find(loginInfo: LoginInfo): Future[Option[RegisteredUser]] = ???
  override def find(userName: String): Future[Option[RegisteredUser]] = ???
  override def save(user: RegisteredUser): Future[RegisteredUser] = ???
  override def add(user: RegisteredUser): Future[RegisteredUser] = ???
  override def update(user: RegisteredUser): Future[RegisteredUser] = ???
  override def remove(loginInfo: LoginInfo): Future[Unit] = ???
  override def remove(userName: String): Future[Unit] = ???
}

