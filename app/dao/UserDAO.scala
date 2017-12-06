package dao

import com.mohiva.play.silhouette.api.LoginInfo
import model.user.RegisteredUser

import scala.concurrent.Future

trait UserDAO{
  def find(loginInfo: LoginInfo): Future[Option[RegisteredUser]]
  def find(userName: String): Future[Option[RegisteredUser]]
  def save(user: RegisteredUser): Future[RegisteredUser]
  def add(user: RegisteredUser): Future[RegisteredUser]
  def update(user: RegisteredUser): Future[RegisteredUser]
  def remove(loginInfo: LoginInfo): Future[Unit]
  def remove(userName: String): Future[Unit]
}

