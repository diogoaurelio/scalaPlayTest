package controllers

/**
 * Created by Diogo on 17/08/2015.
 */

import javax.inject.Inject

import play.api.i18n._
import play.api.mvc._


class Marketing @Inject()(val messagesApi: MessagesApi ) extends Controller with I18nSupport {
  def about = Action {
    Ok(views.html.marketing.about(Messages("global.about_us")))
  }
  def contact = TODO

}
