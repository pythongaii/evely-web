package modules

import com.google.inject.AbstractModule
import dao._
import model.event.APIEvent
import model.user.RegisteredUser
import net.codingwell.scalaguice.ScalaModule
//import service.{UserService, UserServiceImpl}
import play.api.libs.ws.WSResponse

class MyModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[PlainDAO[APIEvent,WSResponse]].to[APIEventDAO]
    bind[Authenticator].to[APIAuthenticator]
    bind[PlainDAO[RegisteredUser,WSResponse]].to[APIUserDAO]
  }


}
