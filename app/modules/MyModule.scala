package modules

import com.google.inject.AbstractModule
import dao._
import forms.CreateEventData
import model.user.RegisteredUser
import net.codingwell.scalaguice.ScalaModule
import play.api.libs.Files
import play.api.mvc.MultipartFormData
import utils._
//import service.{UserService, UserServiceImpl}
import play.api.libs.ws.WSResponse

class MyModule extends AbstractModule with ScalaModule {

  override def configure(): Unit = {
    bind[PlainDAO[CreateEventData,WSResponse]].to[APIEventDAO]
    bind[PlainDAO[RegisteredUser,WSResponse]].to[APIUserDAO]
    bind[Authenticator].to[APIAuthenticator]
    bind[ConfigProvider].to[ConfigProviderImpl]
    bind[Upload[MultipartFormData.FilePart[Files.TemporaryFile]]].to[FileUploader]
  }

}
