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
class UserController @Inject() (
    val controllerComponents: ControllerComponents,
    implicit val ec: ExecutionContext,
    userRepo: UserRepo,
    postRepo: PostRepo,
    commentRepo: CommentRepo,
    queries: Queries,
    postController: PostController
    // commentController: CommentController
) extends BaseController
    with I18nSupport {


        // val users = userRepo.users
        // val posts = postRepo.posts
        // val comments = commentRepo.comments

  
        val userForm = Form(
            mapping(
                "id" -> ignored(UUID.randomUUID()),
                "username" -> nonEmptyText,
                "password" -> nonEmptyText
            )(User.apply)(User.unapply)
        )

        def index() = Action.async { implicit request =>
            for {
            _ <- userRepo.createUserSchema
            _ <- postRepo.createPostSchema
            _ <- commentRepo.createCommentSchema
            } yield Ok(views.html.index())
        }

        def newsfeed() = Action { implicit request =>
            request.session.get("username").map { username =>
                Redirect(routes.UserController.newsfeed())
            }.getOrElse(Redirect(routes.UserController.login()))
        }

        def showUsers() = Action.async { implicit request => {
            queries.getUsers.map (users => Ok(users.mkString("\n")))
            }
        }

        def register() = Action.async { implicit request =>
            userForm.bindFromRequest().fold(
                formWithErrors => Future.successful(BadRequest(views.html.register(userForm))),
                userData => {
                    queries.addUser(userData.copy(userID = UUID.randomUUID())).map(_ => Ok(views.html.register(userForm)))
                }
            )
        }

        def login() = Action.async { implicit request =>
            userForm.bindFromRequest().fold(
                formWithErrors => Future.successful(BadRequest(views.html.login(userForm))),
                userData => {
                    
                    queries.findByUsername(userData.username).map {
                    case Some(user) if user.username == userData.username && user.password == userData.password =>
                        Redirect(routes.UserController.newsfeed()).withSession("username" -> userData.username)
                        //Ok
                    case _ =>
                        BadRequest("Invalid credentials")
                    }
                }
            )
        }

        def logout() = Action { implicit request =>
            Ok(views.html.login(userForm)).withNewSession
        }        

        def loginPage() = Action { implicit request =>
            Ok(views.html.login(userForm))
        }



    }


    