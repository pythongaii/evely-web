package controllers

import javax.inject.Inject

import akka.stream.scaladsl.{FileIO, Source}
import dao.PlainDAO
import forms.{CreateEventData, CreateEventForm}
import play.api.Logger
import play.api.cache.CacheApi
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.Files
import play.api.libs.ws.{WSClient, WSResponse}
import play.api.mvc.{BodyParser, MultipartFormData, Request}
import play.api.mvc.MultipartFormData.FilePart
import utils.{AuthModule, ConfigProvider, Upload}

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class FileUploadController @Inject()(cache: CacheApi,
                                    configProvider: ConfigProvider,
                                     ws:WSClient,
                                     fileUploader:Upload[MultipartFormData.FilePart[Files.TemporaryFile]])(implicit val messagesApi: MessagesApi) extends AuthModule(cache, configProvider) with I18nSupport {

  def fileUpload = withAuth(parse.multipartFormData){ username:String =>
    implicit request:Request[MultipartFormData[Files.TemporaryFile]] =>
      request.body.file("image").map {picture =>
        fileUploader.upload(picture,request).map(res => Ok(res.body))
      }.getOrElse {
        Future.successful(Redirect(routes.GuestHomeController.index).flashing(
          "error" -> "Mossing file"
        ))
      }
  }

}
