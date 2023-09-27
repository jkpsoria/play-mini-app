package models.queries

import java.time.LocalDate
import models.domain._
import models.repo._
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.ExecutionContext
import javax.inject._
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.Future
import java.util.UUID

@Singleton
final class Queries @Inject()(protected val dbConfigProvider: DatabaseConfigProvider, val userRepo: UserRepo, val postRepo: PostRepo, val commentRepo: CommentRepo)(implicit ex: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile]{
    import profile.api._

    val users = userRepo.users
    val posts = postRepo.posts
    val comments = commentRepo.comments
    
    //user
    def getUsers = db.run(users.result)
    def findByUsername(username: String) = db.run(users.filter(_.username === username).result.headOption)
    def addUser(user: User) = db.run(users += user)

    //post
    def getPosts = db.run(posts.result)
    def findByPostID(postID: UUID) = db.run(posts.filter(_.postID === postID).result.headOption)
    def addPost(post: Post) = db.run(posts += post)

    //comment
    def getComments = db.run(comments.result)
    def findByCommentID(commentID: UUID) = db.run(comments.filter(_.id === commentID).result.headOption)
    def addComment(comment: Comment) = db.run(comments += comment)
 
}