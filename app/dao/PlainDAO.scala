package dao

import java.util.UUID

import tokens.SignUpToken

import scala.concurrent.Future

trait PlainDAO[T, U, V] {
  def find(key: U): Future[Option[SignUpToken]]
  def save(obj: V): Future[SignUpToken]
  def add(obj: V): Future[SignUpToken]
  def update(obj: V): Future[SignUpToken]
  def remove(obj: U): Future[Unit]
}
