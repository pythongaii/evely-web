package service

import javax.inject.Inject

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import dao.PasswordInfoDAO
import play.modules.reactivemongo.ReactiveMongoApi

import scala.concurrent.Future


class PasswordInfoService @Inject() (passwordInfoDAO: PasswordInfoDAO) {

  def find(loginInfo: LoginInfo): Future[Option[PasswordInfo]] = passwordInfoDAO.find(loginInfo)

  def save(loginInfo: LoginInfo,
           passwordInfo: PasswordInfo): Future[PasswordInfo] = passwordInfoDAO.save(loginInfo, passwordInfo)

  def remove(loginInfo: LoginInfo): Future[Unit] = passwordInfoDAO.remove(loginInfo)
}
