//package service
//
//import java.util.UUID
//import javax.inject.Inject
//
//import com.mohiva.play.silhouette.api.LoginInfo
//import com.mohiva.play.silhouette.api.services.IdentityService
//import model.User
//import model.user.RegisterdUser
//import modules.CookieEnv
//
//import scala.concurrent.Future
//
//
//abstract class UserService extends IdentityService[User] {
//
//
////  def save(user: RegisterdUser): Future[Option[RegisterdUser]] = ???
////
////  def find(loginInfo: LoginInfo): Future[Option[RegisterdUser]] = ???
////
////  def find(userName: String): Future[Option[RegisterdUser]] = ???
////
////  def Update(user: RegisterdUser): Future[RegisterdUser] = ???
////
////  def retrieve: Future[Option[RegisterdUser]] = ???
////
////  def delete(loginInfo: LoginInfo): Future[Unit] = ???
//}
//
//class UserServiceImpl @Inject() extends UserService {
//  override def retrieve(loginInfo: LoginInfo): Future[Option[User]] = ???
//  def retrieved(id: UUID): Future[Option[User]] = ???
//
//  def save(user: User) = ???
//}
//
