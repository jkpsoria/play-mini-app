package models.repo

import javax.inject._
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{ExecutionContext, Future}
import models.domain._
import java.util.UUID
import java.time.LocalDateTime



@Singleton
final class UserRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit ex: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile]{

    import profile.api._

    protected class UserTable (tag: Tag) extends Table[User](tag, "USERS") {
        def userID = column[UUID]("USER_ID", O.PrimaryKey)
        def username = column[String]("USERNAME", O.Unique)
        def password = column[String]("USER_PASSWORD")
        def * = (userID, username, password).mapTo[User]
    }

    val users = TableQuery[UserTable]

    def createUserSchema = {
      db.run(users.schema.createIfNotExists)
    }

}