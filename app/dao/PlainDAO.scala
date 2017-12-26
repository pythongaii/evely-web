package dao

import play.api.mvc.RequestHeader

import scala.concurrent.Future

trait PlainDAO[V, T] {
  def find(key: String): Future[Option[T]]
  def save(obj: V,request: RequestHeader): Future[T]
  def add(obj: V, request: RequestHeader): Future[T]
  def update(obj: V, request: RequestHeader): Future[T]
  def remove(key: String, requestHeader: RequestHeader): Future[Unit]
}
