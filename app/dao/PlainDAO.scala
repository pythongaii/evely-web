package dao

import java.util.UUID

import play.api.mvc.RequestHeader
import tokens.SignUpToken

import scala.concurrent.Future

trait PlainDAO[U, V, T] {
  def find(key: U): Future[Option[T]]
  def save(obj: V, request: RequestHeader): Future[T]
  def add(obj: V): Future[T]
  def update(obj: V): Future[T]
  def remove(key: U): Future[Unit]
}
