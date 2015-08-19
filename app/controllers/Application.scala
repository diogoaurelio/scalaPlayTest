package controllers

import javax.inject._

import dal._
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n._
import play.api.libs.json._
import play.api.mvc._
import play.api.libs.ws._

import scala.concurrent.{ExecutionContext, Future}


class Application @Inject() (repo: PersonRepository, val messagesApi: MessagesApi, ws: WSClient) (implicit ec: ExecutionContext) extends Controller with I18nSupport {

  val personForm = Form(
    mapping(
      "firstname" -> text,
      "lastname" -> text,
      "email" -> nonEmptyText
    )(PersonData.apply)(PersonData.unapply)
  )

  def index = Action { implicit request =>
    // OK - means http status 200
    Ok(views.html.index(personForm))
  }

  def addPerson = Action.async { implicit request =>
    personForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.index(errorForm)))
      },
      person => {
        repo.create(person.firstname, person.lastname, person.email).map { _ =>
          //Redirect(routes.Application.index())
          emailNewRegistration(person.firstname, person.lastname, person.email)
          Ok(views.html.registration.registrationSuccess(person.firstname, person.lastname, person.email) )
        }
      }
    )
  }

  /**
   * A REST endpoint that gets all the people as JSON.
   */
  def getPersons = Action.async {
    repo.list().map { people =>
      Ok(Json.toJson(people))
    }
  }

  def buildRegistrationTemplate(firstName: String, lastName: String, email: String, mandrilKey: String): JsValue = {
    JsObject(Seq(
      "key" -> JsString(mandrilKey),
      "template_name" -> JsString("my-mandrill-thank-you-template"),
      "template_content" -> JsArray(Seq(
        JsObject(Seq("name" -> JsString("firstname"), "content" -> JsString(firstName))),
        JsObject(Seq("name" -> JsString("appname"), "content" -> JsString(Messages("global.appName")))),
        "message" -> JsObject(
          Seq("to" -> JsArray(Seq("email" -> JsString(email),
            "name" -> JsString(firstName + " " + lastName ),
            "type" -> JsString("to")
          )))
        ),
        "headers" -> JsObject(Seq(
          "Reply-To" -> JsString(Messages("global.ReplyToEmail"))
        )),
        "important" -> JsBoolean(false),
        "track_opens" -> JsBoolean(true)
      ))
    ))
  }
  def emailNewRegistration(firstName: String, lastName: String, email: String) {
    val mandrillKey: String = play.Play.application.configuration.getString("mandrillkey")
    val jsonClass = buildRegistrationTemplate(firstName, lastName, email, mandrillKey)
    val apiUrl = play.Play.application.configuration.getString("mandrillSendViaTemplateURL")
    val futureResponse: Future[WSResponse] = ws.url(apiUrl).post(jsonClass)
  }
}
/**
 * The create person form.
 *
 * Generally for forms, you should define separate objects to your models, since forms very often need to present data
 * in a different way to your models.  In this case, it doesn't make sense to have an id parameter in the form, since
 * that is generated once it's created.
 */
case class PersonData(firstname: String, lastname: String, email: String)
