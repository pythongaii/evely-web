package service

import javax.inject.Inject

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import dao.{MongoPasswordInfoDao, PasswordInfoDAO}
import play.modules.reactivemongo.ReactiveMongoApi

import scala.concurrent.Future


class PasswordInfoService @Inject() (mongoPasswordInfoDao: MongoPasswordInfoDao) {

  def find(loginInfo: LoginInfo): Future[Option[PasswordInfo]] = mongoPasswordInfoDao.find(loginInfo)

  def save(loginInfo: LoginInfo,
           passwordInfo: PasswordInfo): Future[PasswordInfo] = mongoPasswordInfoDao.save(loginInfo, passwordInfo)

  def remove(loginInfo: LoginInfo): Future[Unit] = mongoPasswordInfoDao.remove(loginInfo)
}
