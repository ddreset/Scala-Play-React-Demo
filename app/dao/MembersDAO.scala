package dao

import javax.inject.{ Inject, Singleton }

import scala.concurrent.{ ExecutionContext, Future }
import models.Member
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

@Singleton()
class MembersDAO @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext){

  val dbConfig = dbConfigProvider.get[JdbcProfile]
  
  import dbConfig._
  import profile.api._

  class Members(tag: Tag) extends Table[Member](tag, "MEMBER") {
    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
    def name = column[String]("NAME")
    def clubId = column[Long]("CLUBID")
    def * = (id.?, name, clubId.?) <> ((Member.apply _).tupled, Member.unapply _)
  }

  val members = TableQuery[Members]

  def list(): Future[Seq[Member]] = db.run{
    members.result
  }

  /** Insert a new member */
  def insert(member: Member): Future[Unit] =
    db.run(members += member).map(_ => ())

  /** Insert a list of new members */
  def insert(members: Seq[Member]): Future[Option[Int]] = db.run(this.members ++= members)

}