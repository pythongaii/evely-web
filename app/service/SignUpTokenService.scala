package service

import java.util.UUID
import java.util.concurrent.Future

import forms.SignUpForm
import tokens.SignUpToken


class SignUpTokenService{

  def createUserToken(email: String): Void = ???

  def save(signUpToken: SignUpToken): Future[SignUpToken] = ???

  def find(tokenID: UUID): Future[SignUpToken] = ???

  def delete(tokenID: UUID): Future[Unit] = ???

}

