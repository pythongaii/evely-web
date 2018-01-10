import java.io.File
import javax.inject.Inject

import org.mockito.Mockito._
import org.scalatestplus.play.PlaySpec
import play.api.{Configuration, Environment, Mode}
import play.modules.reactivemongo.ReactiveMongoApi
import utils.{ConfigProvider, ConfigProviderImpl}
import play.api.inject.guice.GuiceApplicationBuilder

class ConfigProviderTest extends PlaySpec {

  val application = new GuiceApplicationBuilder().build
  val secured = application.injector.instanceOf[Configuration]
  "テスト名" must {
    "テスト条件" in {

      val configProvider = ConfigProviderImpl(secured)
      // テストに必要なクラスのモックオブジェクトの作成
      val test = configProvider.EVENT_URL

      // モックで置き換えていないメソッドの
      test mustBe "testMethod"
    }
  }
}
