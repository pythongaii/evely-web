package utils

import javax.inject.Inject

import akka.stream.scaladsl.{FileIO, Source}
import play.api.Logger
import play.api.libs.Files
import play.api.libs.ws.{WSClient, WSResponse}
import play.api.mvc.MultipartFormData
import play.api.mvc.MultipartFormData.FilePart
import play.api.mvc.RequestHeader
import play.mvc.Http
import play.api.http.Status._

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class FileUploader @Inject()(ws: WSClient, configProvider: ConfigProvider) extends Upload[MultipartFormData.FilePart[Files.TemporaryFile]] {

  override def upload(sourceFile: FilePart[Files.TemporaryFile], requestHeader: RequestHeader): Future[WSResponse] = {
    val optionalCookie = requestHeader.cookies.get(configProvider.COOKIE_NAME)

    optionalCookie match {
      case Some(cookie) => {
        val tokenString = cookie.value
        val s = Source(FilePart("image", sourceFile.filename, sourceFile.contentType, FileIO.fromPath(sourceFile.ref.file.toPath)) :: List())
        val res = ws.url("http://160.16.140.145:8888/api/develop/v2/files/upload").
          withHeaders(("Authorization", "Bearer " + tokenString)).
          post(s)
        res.filter(res => res.status == OK).flatMap(res => Future.successful(res))
      }
    }

  }


}
