package modules

import com.google.inject.{AbstractModule, Provides}
import com.mohiva.play.silhouette.api._
import com.mohiva.play.silhouette.api.crypto._
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.services._
import com.mohiva.play.silhouette.api.util._
import com.mohiva.play.silhouette.crypto.{JcaCookieSigner, JcaCookieSignerSettings, JcaCrypter, JcaCrypterSettings}
import com.mohiva.play.silhouette.impl.authenticators._
import com.mohiva.play.silhouette.impl.providers._
import com.mohiva.play.silhouette.impl.util._
import com.mohiva.play.silhouette.password.BCryptPasswordHasher
import com.mohiva.play.silhouette.persistence.daos.DelegableAuthInfoDAO
import com.mohiva.play.silhouette.persistence.repositories.DelegableAuthInfoRepository
import dao._
import model.event.APIEvent
import model.user.RegisteredUser
import net.codingwell.scalaguice.ScalaModule
import play.api.Configuration
import play.api.libs.concurrent.Execution.Implicits._
//import service.{UserService, UserServiceImpl}
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._
import play.api.libs.ws.WSResponse
import reactivemongo.api.{DB, MongoConnectionOptions, MongoDriver}

class SilhouetteModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
//    bind[UserDAO].to[MongoUserDAO]
    bind[PlainDAO[String,APIEvent,WSResponse]].to[APIEventDAO]
    bind[PasswordInfoDAO].to[MongoPasswordInfoDao]
//    bind[UserService].to[UserServiceImpl]
    bind[Authen].to[APIAuthenticator]
    bind[PlainDAO[String,RegisteredUser,WSResponse]].to[APIUserDAO]
//    bind[DB].toInstance {
//      import com.typesafe.config.ConfigFactory
//
//      import scala.collection.JavaConversions._
//      import scala.concurrent.ExecutionContext.Implicits.global
//
//      val config = ConfigFactory.load
//      val driver = new MongoDriver
//      val connection = driver.connection(
//        config.getStringList("mongodb.servers"),
//        MongoConnectionOptions(),
//        Seq()
//      )
//      connection.db(config.getString("mongodb.db"))
//    }
    bind[CacheLayer].to[PlayCacheLayer]
    bind[IDGenerator].toInstance(new SecureRandomIDGenerator())
    bind[PasswordHasher].toInstance(new BCryptPasswordHasher)
    bind[FingerprintGenerator].toInstance(new DefaultFingerprintGenerator(false))
    bind[DelegableAuthInfoDAO[PasswordInfo]].to[PasswordInfoDAO]
    bind[AuthenticatorEncoder].toInstance(new Base64AuthenticatorEncoder)
    bind[EventBus].toInstance(EventBus())
    bind[Clock].toInstance(Clock())
  }


}
