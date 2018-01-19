package dao

import play.api.mvc.RequestHeader

import scala.concurrent.Future

trait PlainDAO[OBJ_TYPE, RES_TYPE] {
  def find(requestHeader: RequestHeader, key: (String, String)*): Future[Option[RES_TYPE]]
  def save(obj: OBJ_TYPE,request: RequestHeader): Future[RES_TYPE]
  def add(obj: OBJ_TYPE, request: RequestHeader): Future[RES_TYPE]
  def update(obj: OBJ_TYPE, request: RequestHeader): Future[RES_TYPE]
  def remove(requestHeader: RequestHeader,key: (String, String)*): Future[Unit]
}
