package com.ntny.web

import cats.effect.{Blocker, ExitCode, IO, IOApp}
import com.ntny.web.features.categories.CategoriesRoutes
import com.ntny.web.features.links.LinksRoutes
import com.ntny.web.infrastracture.PostgresTransactor
import org.http4s.HttpApp
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder

import scala.concurrent.ExecutionContext

object Application extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    val blocker: Blocker = Blocker.liftExecutionContext(ExecutionContext.global)
    PostgresTransactor[IO](Config.jdbcUrl, Config.databaseUserName, Config.databasePassword)(blocker).use{ xa =>
      val router = Router(
        version.v1 -> new LinksRoutes[IO](xa).routes,
        version.v1 -> new CategoriesRoutes[IO](xa).routes
      )

      import org.http4s.implicits._
      val httpApp: HttpApp[IO] = router.orNotFound

      import scala.concurrent.ExecutionContext.Implicits.global
      val serverBuilder = BlazeServerBuilder[IO](global).bindHttp(8080, "localhost").withHttpApp(httpApp)
      serverBuilder
        .serve
        .compile
        .drain
        .map(_ => ExitCode.Success)
    }
  }
}
