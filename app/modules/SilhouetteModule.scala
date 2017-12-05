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
import model.user.RegisterdUser
import net.codingwell.scalaguice.ScalaModule
import play.api.Configuration
import play.api.libs.concurrent.Execution.Implicits._
import service.{UserService, UserServiceImpl}
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._
import reactivemongo.api.{DB, MongoConnectionOptions, MongoDriver}

trait CookieEnv extends Env {
  type I = RegisterdUser
  type A = CookieAuthenticator
}

class SilhouetteModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[Silhouette[CookieEnv]].to[SilhouetteProvider[CookieEnv]]
    bind[UserDAO].to[MongoUserDAO]
    bind[UserService].to[UserServiceImpl]
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

  @Provides def provideEnvironment(
                                    userService: UserService,
                                    authenticatorService: AuthenticatorService[CookieAuthenticator],
                                    eventBus: EventBus): Environment[CookieEnv] = {
    Environment[CookieEnv](userService, authenticatorService, Seq(), eventBus)
  }

  /**
    * Provides the cookie signer for the authenticator.
    *
    * @param configuration The Play configuration.
    * @return The cookie signer for the authenticator.
    */
  @Provides
  def provideAuthenticatorCookieSigner(configuration: Configuration): CookieSigner = {
    val config = configuration.underlying.as[JcaCookieSignerSettings]("silhouette.authenticator.cookie.signer")
    new JcaCookieSigner(config)
  }

  /**
    * Provides the crypter for the authenticator.
    *
    * @param configuration The Play configuration.
    * @return The crypter for the authenticator.
    */
  @Provides
  def provideAuthenticatorCrypter(configuration: Configuration): Crypter = {
    val config = configuration.underlying.as[JcaCrypterSettings]("silhouette.authenticator.crypter")
    new JcaCrypter(config)
  }

  /**
    * Provides the auth info repository.
    *
    * @param passwordInfoDAO The implementation of the delegable password auth info DAO.
    * @return The auth info repository instance.
    */
  @Provides
  def provideAuthInfoRepository(passwordInfoDAO: DelegableAuthInfoDAO[PasswordInfo]): AuthInfoRepository = {
    new DelegableAuthInfoRepository(passwordInfoDAO)
  }

  /**
    * Provides the authenticator service.
    *
    * @param cookieSigner The cookie signer implementation.
    * @param crypter The crypter implementation.
    * @param fingerprintGenerator The fingerprint generator implementation.
    * @param idGenerator The ID generator implementation.
    * @param configuration The Play configuration.
    * @param clock The clock instance.
    * @return The authenticator service.
    */
  @Provides
  def provideAuthenticatorService(
                                   cookieSigner: CookieSigner,
                                   crypter: Crypter,
                                   fingerprintGenerator: FingerprintGenerator,
                                   idGenerator: IDGenerator,
                                   configuration: Configuration,
                                   clock: Clock
                                 ): AuthenticatorService[CookieAuthenticator] = {
    val config = configuration.underlying.as[CookieAuthenticatorSettings]("silhouette.authenticator")
    val encoder = new CrypterAuthenticatorEncoder(crypter)
    new CookieAuthenticatorService(config, None, cookieSigner, encoder, fingerprintGenerator, idGenerator, clock)
  }

  /**
    * Provides the password hasher registry.
    *
    * @param passwordHasher The default password hasher implementation.
    * @return The password hasher registry.
    */
  @Provides
  def providePasswordHasherRegistry(passwordHasher: PasswordHasher): PasswordHasherRegistry = {
    new PasswordHasherRegistry(passwordHasher)
  }

  /**
    * Provides the credentials provider.
    *
    * @param authInfoRepository The auth info repository implementation.
    * @param passwordHasherRegistry The password hasher registry.
    * @return The credentials provider.
    */
  @Provides
  def provideCredentialsProvider(
                                  authInfoRepository: AuthInfoRepository,
                                  passwordHasherRegistry: PasswordHasherRegistry
                                ): CredentialsProvider = {
    new CredentialsProvider(authInfoRepository, passwordHasherRegistry)
  }
}
