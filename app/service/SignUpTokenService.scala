package service

import java.util.UUID
import javax.inject.Inject

import dao.{MongoSignUpTokenDAO, SignUpTokenDAO}
import org.joda.time.DateTime
import tokens.SignUpToken

import scala.concurrent.Future


class SignUpTokenService @Inject() (mongoSignUpTokenDAO: MongoSignUpTokenDAO) {

  def createUserToken(mailaddress: String, isSignUp: Boolean): SignUpToken = {
    SignUpToken(UUID.randomUUID(), mailaddress, new DateTime().plusHours(24), isSignUp)
  }


  def save(signUpToken: SignUpToken): Future[SignUpToken] = mongoSignUpTokenDAO.save(signUpToken)

  def find(tokenID: UUID): Future[Option[SignUpToken]] = mongoSignUpTokenDAO.find(tokenID)

  def remove(tokenID: UUID): Future[Unit] = mongoSignUpTokenDAO.remove(tokenID)

}

