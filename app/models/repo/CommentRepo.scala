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
final class CommentRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider, val postRepo: PostRepo, val userRepo: UserRepo)(implicit ex: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile]{

    import profile.api._

    protected class CommentTable (tag: Tag) extends Table[Comment](tag, "COMMENTS") {
        def id = column[UUID]("ID", O.PrimaryKey)
        def comment = column[String]("POST_COMMENT")
        def postID = column[UUID]("POST_ID")

        def * = (id, comment, postID).mapTo[Comment]

        def postFKey = foreignKey("FK_POST_ID", postID, postRepo.posts)(_.postID, onDelete = ForeignKeyAction.Cascade)    }


    val comments = TableQuery[CommentTable]

    def createCommentSchema = {
      db.run(comments.schema.createIfNotExists)
    }

}