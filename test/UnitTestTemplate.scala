import javax.inject.Inject

import com.mohiva.play.silhouette.api.Silhouette
import modules.CookieEnv
import play.modules.reactivemongo.ReactiveMongoApi
import org.mockito.Mockito._

/**
  * 単体テスト用のテンプレート
  * @param reactiveMongoApi DB接続に必要、不要なら消してかまわない
  * @param silhouette 認証に必要、不要なら消してかまわない
  */
class UnitTestTemplate @Inject()(val reactiveMongoApi: ReactiveMongoApi, silhouette: Silhouette[CookieEnv]) extends UnitTestBase {
  "テスト名" must {
    "テスト条件" in {
      // テストに必要なクラスのモックオブジェクトの作成
      val test = mock[Test]
      // モックオブジェクトのメソッドの返り値を指定、
      when(test.testMethod()) thenReturn "mockTest"

      // モックオブジェクトを使用して、テストしたいクラスの生成
      val target = new Test{
        override val mockTest: Test = test
      }

      // モックで置き換えたメソッドの実行
      target.mockTest.testMethod() mustBe "mockTest"
      // モックで置き換えていないメソッドの実行
      target.testMethod() mustBe "testMethod"
    }
  }
}
