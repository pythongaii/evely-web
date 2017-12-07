package utils

import javax.inject.Inject

import play.api.i18n.Messages

import scala.concurrent.Future
import play.api.libs.mailer._

class Mailer @Inject()(configuration: SMTPConfiguration, mailer: MailerClient){
  val from = configuration.host
  val replyTo = configuration.user

  def sendEmailAsync(recipients: String*)(subject: String,
                      bodyHtml: Option[String],
                      bodyText: Option[String]
                     ) = {
    Future {
      sendEmail(recipients:_*)(subject, bodyHtml, bodyText)
    } recover {
      case e => play.api.Logger.error("error sending email", e)
    }
  }

  def sendEmail(recipients: String*)(subject: String,
                 bodyHtml: Option[String], bodyText: Option[String]
                ) = {
    val email = Email(subject, from, recipients, bodyText, bodyHtml, replyTo = replyTo)
    mailer.send(email)
  }

  def confirm(mailAddress: String, link: String)(implicit messages: Messages) = {
    sendEmailAsync(mailAddress)(
      subject = Messages("mail.confirm.subject"),
      bodyHtml = Some(views.html.mails.confirm(link).toString),
      bodyText = Some(views.html.mails.confirm_text(link).toString)
    )
  }

  def resetPassword(mailAddress: String, link: String)(implicit messages: Messages) =  {
    sendEmailAsync(mailAddress)(
      subject = Messages("mail.resetpassword.subject"),
      bodyHtml = Some(views.html.mails.reset_password(link).toString),
      bodyText = Some(views.html.mails.reset_password_text(link).toString)
    )
  }
}

