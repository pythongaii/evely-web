package dao

import java.util.UUID

import tokens.SignUpToken

import scala.concurrent.Future

trait SignUpTokenDAO {
  def find(tokenId: UUID): Future[Option[SignUpToken]]
  def save(token: SignUpToken): Future[SignUpToken]
  def add(token: SignUpToken): Future[SignUpToken]
  def update(token: SignUpToken): Future[SignUpToken]
  def remove(token: SignUpToken): Future[SignUpToken]
}
