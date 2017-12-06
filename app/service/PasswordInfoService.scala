package service

import java.util.concurrent.Future
import javax.inject.Inject

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import play.modules.reactivemongo.ReactiveMongoApi


class PasswordInfoService @Inject() (reactiveMongoApi: ReactiveMongoApi) {

  def find(loginInfo: LoginInfo): Future[Option[PasswordInfo]] = ???

  def save(loginInfo: LoginInfo,
           passwordInfo: PasswordInfo): Future[PasswordInfo] = ???

  def add(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = ???

  def update(loginInfo: LoginInfo,
             PasswordInfo: PasswordInfo): Future[PasswordInfo] = ???

  def remove(loginInfo: LoginInfo): Future[Unit] = ???
}
