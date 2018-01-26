package utils

import akka.stream.scaladsl.Source
import play.api.libs.ws.WSResponse
import play.api.mvc.RequestHeader

import scala.concurrent.Future

trait Upload[FileType] {
  def upload(sourceFile: FileType, requestHeader: RequestHeader) : Future[WSResponse];
}
