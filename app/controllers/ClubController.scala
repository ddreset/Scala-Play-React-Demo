package controllers

import javax.inject.Inject

import play.api.libs.json.{ JsNull, Json, JsString, JsValue, JsError, JsArray, JsNumber, JsObject }
import play.api.mvc._
import play.api.mvc.{ AbstractController, ControllerComponents }
import play.api.data.Form
import play.api.data.Forms._
import scala.concurrent.{ExecutionContext, Future, Await}
import scala.concurrent.duration._

import dao.{ClubsDAO, MembersDAO}
import models.{Club, Member}

import play.api.Logger

class ClubController @Inject()(clubsDao: ClubsDAO, membersDao: MembersDAO, cc: ControllerComponents)(implicit executionContext: ExecutionContext) extends AbstractController(cc) {

  private val logger = Logger(getClass)

  /**
   * The mapping for the club form.
   */
  val clubForm = Form (
    mapping(
      "id" -> optional(longNumber),
      "name" -> nonEmptyText(),
      "abbr" -> text()
    )(Club.apply)(Club.unapply))
  
  val memberForm = Form (
    mapping(
      "id" -> optional(longNumber),
      "name" -> nonEmptyText(),
      "clubId" -> optional(longNumber)
    )(Member.apply)(Member.unapply))

  def listClubs = Action.async { implicit request =>
    clubsDao.leftJoin().map{club => Ok(Json.toJson(club))}
  }

  def createClub = Action.async(parse.json) { implicit request =>
    val newClub = request.body \ "club"
    var clubId: Long = 0

    newClub.validate[Club].fold(
      errors => {
        Future(BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors))))
      },
      club => {
        // flatMap for nested Future
        // clubsDao.insert(club).flatMap(clubId => {
        //   // use clubId to insert members
        // })

        // save club and return clubId
        val insertClub: Future[(Long)] = 
          for { result <- clubsDao.insert(club)} yield (result)
        val (clubId) = Await.result(insertClub, Duration(4, SECONDS))
        // set clubId for each member
        var members:JsArray = (request.body \ "members").as[JsArray]
        val newMembers = members.value.map( member => {
          Json.obj("name" -> member.\("name").as[String],"clubId" -> clubId)
        })
        
        // save members
        Json.toJson(newMembers).validate[List[Member]].fold(
          errors => {
            Future(BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors))))
          },
          members => {
            membersDao.insert(members).map{
              case Some(num) => {Ok(Json.obj("status" -> "OK", "clubId" -> clubId, "message" -> (num + " members saved.")))}
              case None => {Ok(Json.obj("status" -> "KO", "clubId" -> clubId, "message" -> "no members saved"))}
            }
          }
        )
      }
    )
  }
}