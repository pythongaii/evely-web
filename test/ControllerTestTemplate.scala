import javax.inject.Inject

import controllers.TopController
import play.api.mvc.{Result, Results}
import play.api.test.FakeRequest
import play.api.test.Helpers._

import play.api.i18n.{I18nSupport, MessagesApi}

import scala.concurrent.Future

class ControllerTestTemplate @Inject()()(implicit val messagesApi: MessagesApi)extends UnitTestBase with Results with I18nSupport {

  "test pageName" should {
    "test 条件" in {
      // テストしたいControllerのオブジェクトの作成
//      val targetController = new TopController()
//
//      // 作成したControllerのActionメソッドを実行。引数にリクエストのダミーを渡す
//      val result:Future[Result] = targetController.index().apply(FakeRequest())
//
//      // リザルトを取得
//      val bodyText = contentAsString(result)
//
//      // 期待するテスト条件を確かめる
//      // status等の各種メソッドはplay.api.test.Helpersに定義されている
//      status(result) mustBe OK
//      contentType(result) mustBe Some("text/html")
//      bodyText must contain("ok")
    }

  }

}
