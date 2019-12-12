package dao

import javax.inject.{ Inject, Singleton }

import scala.concurrent.{ ExecutionContext, Future }
import models.{Club, Member}
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

// trait ClubsComponent { self: HasDatabaseConfigProvider[JdbcProfile] =>
//   import profile.api._

  
// }

@Singleton()
class ClubsDAO @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
  // extends ClubsComponent
  // with HasDatabaseConfigProvider[JdbcProfile] 
  {

  val dbConfig = dbConfigProvider.get[JdbcProfile]
  
  import dbConfig._
  import profile.api._

  class Clubs(tag: Tag) extends Table[Club](tag, "CLUB") {
    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
    def name = column[String]("NAME")
    def abbr = column[String]("ABBR")
    def * = (id.?, name, abbr) <> ((Club.apply _).tupled, Club.unapply _)
  }

  class Members(tag: Tag) extends Table[Member](tag, "MEMBER") {
    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
    def name = column[String]("NAME")
    def clubId = column[Long]("CLUBID")
    def club = foreignKey("club_fk", clubId, clubs)(_.id)
    def * = (id.?, name, clubId.?) <> ((Member.apply _).tupled, Member.unapply _)
  }

  val clubs = TableQuery[Clubs]
  val members = TableQuery[Members]

  /** Construct the Map[String,String] needed to fill a select options set */
  // def list(): Future[Seq[Club]] = {
  //   val query = (for {
  //     club <- clubs
  //   } yield (club.id, club.name, club.abbr)).sortBy( /*name*/ _._2)

  //   db.run(query.result).map(rows => rows.map { case (id, name, abbr) => (id.toString, name, abbr) })
  // }

  def list(): Future[Seq[Club]] = db.run{
    clubs.result
  }

  /** Insert a new club */
  def insert(club: Club): Future[Long] = db.run{
    (clubs returning clubs.map(_.id)) += club
  }

  /** Insert a list of new clubs */
  def insert(clubs: Seq[Club]): Future[Option[Int]] = db.run(this.clubs ++= clubs)

  /** count clubs*/  
  def count(): Future[Int] = db.run(clubs.length.result)

  // query for leftJoin
  // def leftJoin (): Future[Seq[(Club,Option[Member])]] = db.run {
  //   clubs.joinLeft(members).on(_.id === _.clubId).result
  // }

  def leftJoin (): Future[Seq[(Club,Seq[Option[Member]])]] = {
    db.run(clubs.joinLeft(members).on(_.id === _.clubId).result).map(_
      .groupBy(_._1)
      .mapValues(_.map(_._2))
      .toSeq
      .sortBy(_._1.id))
  }
}
