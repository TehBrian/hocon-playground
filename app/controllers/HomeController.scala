package controllers

import com.typesafe.config._
import play.api.mvc._

import javax.inject._
import scala.util.{Failure, Success, Try}

@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  private val confResolverOptions = ConfigResolveOptions.noSystem()

  def hoconToJson(): Action[String] = Action(parse.tolerantText) { implicit request =>
    val resolve = request.queryString.getOrElse("resolve", Seq()).headOption.exists(_.toBoolean)
    val json = request.queryString.getOrElse("json", Seq()).headOption.exists(_.toBoolean)
    val formatted = request.queryString.getOrElse("formatted", Seq()).headOption.exists(_.toBoolean)
    val comments = request.queryString.getOrElse("comments", Seq()).headOption.exists(_.toBoolean)
    val originComments = request.queryString.getOrElse("originComments", Seq()).headOption.exists(_.toBoolean)
    val parseOptions = ConfigParseOptions.defaults().setIncluder(new ProhibitIncluder())
    val conf = Try(ConfigFactory.parseString(request.body, parseOptions))

    conf match {
      case Success(conf) =>
        val resolvedConf = if (resolve) conf.resolve(confResolverOptions) else conf
        val renderConfig = ConfigRenderOptions.defaults()
          .setJson(json)
          .setFormatted(formatted)
          .setComments(comments)
          .setOriginComments(originComments)
        val rendered = resolvedConf.root().render(renderConfig)
        Ok(rendered)
      case Failure(err) => Ok(err.getMessage)
      case _ => Ok("internal error")
    }
  }
}

