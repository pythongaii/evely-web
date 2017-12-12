import javax.inject.Inject

import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import controllers.{SignInController, TopController}
import modules.CookieEnv
import org.mockito.Mockito
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import play.api.mvc.{Result, Results}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.ws.{WSClient, WSRequest}
import service.{PasswordInfoService, UserService}

import scala.concurrent.Future

class ControllerTestTemplate extends PlaySpec with Results with I18nSupport with MockitoSugar {

  val silhouette = mock[Silhouette[CookieEnv]]
  val userService = mock[UserService]
  val passwordInfoService = mock[PasswordInfoService]
  val credentialsProvider = mock[CredentialsProvider]
  val ws = new WSClient {override def underlying[T]: T = ???

    override def url(url: String): WSRequest = ???

    override def close(): Unit = ???
  }
  implicit val messagesApi = mock[MessagesApi]

  "test pageName" should {
    "test 条件" in {
      // テストしたいControllerのオブジェクトの作成
      val targetController = new SignInController(silhouette,userService,passwordInfoService,credentialsProvider,ws)

      // 作成したControllerのActionメソッドを実行。引数にリクエストのダミーを渡す
      val result:Future[Result] = targetController.apisignin().apply(FakeRequest())

      // リザルトを取得
      val bodyText = contentAsString(result)

      // 期待するテスト条件を確かめる
      // status等の各種メソッドはplay.api.test.Helpersに定義されている
      status(result) mustBe OK
      contentType(result) mustBe Some("text/html")
      bodyText must contain("ok")
    }

  }

}
