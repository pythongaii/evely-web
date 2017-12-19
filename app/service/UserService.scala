//package service
//
//import java.util.UUID
//import javax.inject.Inject
//
//import com.mohiva.play.silhouette.api.LoginInfo
//import com.mohiva.play.silhouette.api.services.IdentityService
//import dao.UserDAO
//import model.user.RegisteredUser
//
//import scala.concurrent.Future
//
//trait UserService {
//
//  def save(user: RegisteredUser): Future[RegisteredUser]
//
//  def find(loginInfo: LoginInfo): Future[Option[RegisteredUser]]
//
//  def find(userName: String): Future[Option[RegisteredUser]]
//
//  def update(user: RegisteredUser): Future[RegisteredUser]
//
//  def remove(loginInfo: LoginInfo): Future[Unit]
//}
//
//class UserServiceImpl @Inject()(userDAO: UserDAO) extends UserService {
//
//  override def find(loginInfo: LoginInfo): Future[Option[RegisteredUser]]  = userDAO.find(loginInfo)
//
//  override def find(userName: String): Future[Option[RegisteredUser]] = userDAO.find(userName)
//
//  override def update(user: RegisteredUser): Future[RegisteredUser] = userDAO.update(user)
//
//  override def remove(loginInfo: LoginInfo): Future[Unit] = userDAO.remove(loginInfo)
//
//  override def save(user: RegisteredUser): Future[RegisteredUser] = userDAO.save(user)
//}
//
