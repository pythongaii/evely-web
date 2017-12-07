package service

import java.util.UUID
import javax.inject.Inject

import dao.{MongoSignUpTokenDAO, SignUpTokenDAO}
import tokens.SignUpToken

import scala.concurrent.Future


class SignUpTokenService @Inject() (mongoSignUpTokenDAO: MongoSignUpTokenDAO) {

  def createUserToken(email: String): Void = ???

  def save(signUpToken: SignUpToken): Future[SignUpToken] = mongoSignUpTokenDAO.save(signUpToken)

  def find(tokenID: UUID): Future[Option[SignUpToken]] = mongoSignUpTokenDAO.find(tokenID)

  def remove(tokenID: UUID): Future[Unit] = mongoSignUpTokenDAO.remove(tokenID)

}

