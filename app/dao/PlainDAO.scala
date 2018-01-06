package dao

import play.api.mvc.RequestHeader

import scala.concurrent.Future

trait PlainDAO[OBJ_TYPE, RES_TYPE] {
  def find(key: String): Future[Option[RES_TYPE]]
  def save(obj: OBJ_TYPE,request: RequestHeader): Future[RES_TYPE]
  def add(obj: OBJ_TYPE, request: RequestHeader): Future[RES_TYPE]
  def update(obj: OBJ_TYPE, request: RequestHeader): Future[RES_TYPE]
  def remove(key: String, requestHeader: RequestHeader): Future[Unit]
}
