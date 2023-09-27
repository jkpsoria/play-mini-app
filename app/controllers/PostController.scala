package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import models.domain._
import models.repo._
import models.queries._
import scala.concurrent.ExecutionContext
import play.api.i18n.I18nSupport
import play.api.libs.Files._
import scala.concurrent.Future
import play.api.data._
import play.api.data.Forms._
import java.nio.file.Files
import java.util.UUID
import java.nio.file.Paths
/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's newsfeed page.
 */
@Singleton
class PostController @Inject() (
    val controllerComponents: ControllerComponents,
    implicit val ec: ExecutionContext,
    userRepo: UserRepo,
    commentRepo: CommentRepo,
    queries: Queries,
    // commentController: CommentController

) extends BaseController
    with I18nSupport {


        val postForm = Form (
            mapping(
                "postID" -> ignored(UUID.randomUUID()),
                "description" -> nonEmptyText,
                "userID" -> ignored(UUID.randomUUID())
            )(Post.apply)(Post.unapply))


        
        def show() = Action.async { implicit request =>

            queries.getPosts.map { posts => 
                Ok(posts.mkString("\n"))    
            }
        }

      

        def create(id: UUID) = Action.async { implicit request =>
            postForm.bindFromRequest().fold(
                formWithErrors => Future.successful(BadRequest),
                posts => {
                    val post = queries.getPosts.map(posts => posts.toList)
                    queries.addPost(posts.copy(postID = UUID.randomUUID(), userID = id)).map(_ => Ok)
                    //todo laksfnaksjfnaklshdaSBDjakdhaSD;SDFSDF!!!!
                }
            )
        }



        
    }


    