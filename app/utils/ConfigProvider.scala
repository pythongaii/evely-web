package utils

import javax.inject.Inject

import play.api.Configuration

trait ConfigProvider {
  def COOKIE_NAME:String

  def PUBLICKEY:String

  def EVENT_URL:String

  def SIGNUP_URL:String
  def SIGNUP_MAIL_URL:String

  def SIGNIN_URL:String

  def VERIFY_TOKEN_URL:String
}

case class ConfigProviderImpl @Inject()(config: Configuration) extends ConfigProvider {

  def COOKIE_NAME = config.getString("session.cookieName").get

  def PUBLICKEY = config.getString("api.publicKey").get

  def EVENT_URL = config.getString("api.eventURL").get

  def SIGNUP_URL = config.getString("api.signUpURL").get
  def SIGNUP_MAIL_URL= config.getString("api.signUpMailURL").get

  def SIGNIN_URL = config.getString("api.signInURL").get

  def VERIFY_TOKEN_URL = config.getString("api.verifyTokenURL").get
}
