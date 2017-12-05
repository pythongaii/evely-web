package model.formaction

import play.api.libs.json.Json

case class MailAddress(prefix: String, suffix: String) extends VerificationNeeded {

  def doAction(): Void = ???

  override def verify(): Boolean = ???

  override def isVerified(): Boolean = ???
}

object MailAddress {
  implicit val MailAddressJsonFormat = Json.format[MailAddress]
}
