package service

import java.util.UUID
import javax.inject.Inject

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService
import model.user.RegisterdUser

import scala.concurrent.Future


abstract class UserService extends IdentityService[RegisterdUser] {


  def save(user: RegisterdUser): Future[Option[RegisterdUser]] = ???

  def find(loginInfo: LoginInfo): Future[Option[RegisterdUser]] = ???

  def find(userName: String): Future[Option[RegisterdUser]] = ???

  def Update(user: RegisterdUser): Future[RegisterdUser] = ???

  def retrieve: Future[Option[RegisterdUser]] = ???

  def delete(loginInfo: LoginInfo): Future[Unit] = ???
}

class UserServiceImpl @Inject() extends UserService {
  override def retrieve(loginInfo: LoginInfo): Future[Option[RegisterdUser]] = ???
  def retrieved(id: UUID): Future[Option[RegisterdUser]] = ???

  override def save(user: RegisterdUser) = ???
}

