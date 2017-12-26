package utils

import play.api.Configuration

trait Config extends Injecter {
  val config = injecter.getInstance(classOf[Configuration])
}

object ConfigProvider extends Config {

  val COOKIE_NAME = config.getString("session.CookieName").get

  val PUBLICKEY = config.getString("api.publicKey").get

  val EVENT_URL = config.getString("api.eventURL").get

  val SIGNUP_URL = config.getString("api.signUpURL").get
}
