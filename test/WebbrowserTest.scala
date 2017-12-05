import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play._

class WebbrowserTest extends PlaySpec with OneBrowserPerSuite with OneServerPerSuite with InternetExplorerFactory {

  System.setProperty("webdriver.chrome.driver",".\\drivers\\chromedriver.exe")
//  System.setProperty("webdriver.firefox.driver","")

  "test" must {
    "test" in {
      go to (s"http://localhost:$port/")
      pageTitle mustBe "test123"
      click on find(name("signuplink")).value
      eventually { pageTitle mustBe "signup12"}
    }
  }

}
