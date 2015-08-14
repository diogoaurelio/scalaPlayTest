
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.test._
import play.api.test.Helpers._
import play.api.i18n.Messages @RunWith(classOf[JUnitRunner])


/**
 * Created by Diogo on 14/08/2015.
 */

class MarketingSpec extends Specification { "should display certain elements on the marketing pages" in new WithBrowser {
  about page browser.goTo("http://localhost:" + port + "/about") browser.$("h1").first.getText must equalTo("About us") }

}