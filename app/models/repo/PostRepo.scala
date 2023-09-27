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
final class PostRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider, val userRepo: UserRepo)(implicit ex: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile]{

    import profile.api._

    protected class PostTable (tag: Tag) extends Table[Post](tag, "POSTS") {
        def postID = column[UUID]("POST_ID", O.PrimaryKey)
        def description = column[String]("POST_DESCRIPTION")
        def userID = column[UUID]("USER_ID")
        def * = (postID, description, userID).mapTo[Post]

        def userIDFKey = foreignKey("FK_USER_ID", userID, userRepo.users)(_.userID, onDelete = ForeignKeyAction.Cascade)
    }


    val posts = TableQuery[PostTable]

    def createPostSchema = {
      db.run(posts.schema.createIfNotExists)
    }

}