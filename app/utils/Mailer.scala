package utils

class Mailer(from: String, replyTo: String) {

  def sendEmailAsync(recipient: String, subject: String,
                      bodyHtml: Option[String],
                      bodyText: Option[String]
                     ): Void = ???

  def sendEmail(recipient: String, subject: String,
                 bodyHtml: Option[String], bodyText: Option[String]
                ): Void = ???

  def confirm(mailAddress: String, link: String): Void = ???

  def resetPassword(mailAddress: String, link: String): Void = ???
}

