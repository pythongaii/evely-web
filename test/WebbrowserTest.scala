import dao.APIAuthenticator
import forms.SignInForm
import forms.SignInData
import org.scalatest.AsyncFeatureSpec
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play._
import play.api.mvc.Results
import play.api.mvc.Results
import play.api.test.Helpers._

import scala.concurrent.Await
import scala.concurrent.duration.Duration



class WebbrowserTest extends PlaySpec with OneBrowserPerSuite with ChromeFactory with OneServerPerSuite with MockitoSugar with Results {

  System.setProperty("webdriver.chrome.driver",".\\drivers\\chromedriver.exe")
  //  System.setProperty("webdriver.firefox.driver","")

    "connection test" must {
      "test" in {
        val authenticator = mock[APIAuthenticator]
        val form = SignInForm.signInForm.fill(SignInData("yKicchan", "password"))

        val response = authenticator.signin(form)
        response mustBe null
      }
    }

//  "test" must {
//    "test" in {
//      go to (s"http://localhost:$port/")
//      pageTitle mustBe "test123"
//      click on find(name("signuplink")).value
//      eventually { pageTitle mustBe "signup12"}
//    }
//  }

}
