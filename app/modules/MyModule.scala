package modules

import com.google.inject.AbstractModule
import dao._
import forms.CreateEventData
import model.event.APIEvent
import model.user.RegisteredUser
import net.codingwell.scalaguice.ScalaModule
import play.api.Configuration
import utils.{ConfigProvider, ConfigProviderImpl}
//import service.{UserService, UserServiceImpl}
import play.api.libs.ws.WSResponse

class MyModule extends AbstractModule with ScalaModule {

  override def configure(): Unit = {
    bind[PlainDAO[CreateEventData,WSResponse]].to[APIEventDAO]
    bind[PlainDAO[RegisteredUser,WSResponse]].to[APIUserDAO]
    bind[Authenticator].to[APIAuthenticator]
    bind[ConfigProvider].to[ConfigProviderImpl]
  }

}