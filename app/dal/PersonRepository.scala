package dal

/**
 * Created by Diogo on 12/08/2015.
 */

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import models.Person
import scala.concurrent.{Future, ExecutionContext}

/**
 * A repository for people.
 *
 * @param dbConfigProvider The Play db config provider. Play will inject this for you.
 */


@Singleton
class PersonRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import driver.api._

  /**
   * Here we define the table. It will have a name of people
   */
  private class PeopleTable(tag: Tag) extends Table[Person](tag, "people") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    //def id = column[Int]("id")

    def firstname = column[String]("firstname")

    def lastname = column[String]("lastname")

    def email = column[String]("email")

    /**
     * This is the tables default "projection".
     *
     * It defines how the columns are converted to and from the Person object.
     *
     * In this case, we are simply passing the id, name and page parameters to the Person case classes
     * apply and unapply methods.
     */
    def * = (id, firstname, lastname, email) <> ((Person.apply _).tupled, Person.unapply)
  }
  private val people = TableQuery[PeopleTable]

  def create(firstname: String, lastname: String, email: String): Future[Person] = db.run {
    (people.map(p => (p.firstname, p.lastname, p.email))
      returning people.map(_.id)
      into ((firstnameLastnameEmail, id) => Person(id, firstnameLastnameEmail._1, firstnameLastnameEmail._2, firstnameLastnameEmail._3))
      ) += (firstname, lastname, email)
  }

  /**
   * List all the people in the database.
   */
  def list(): Future[Seq[Person]] = db.run {
    people.result
  }

}
