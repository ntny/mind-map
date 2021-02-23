package com.ntny.web

import cats.effect.{ExitCode, IO, IOApp}
import com.ntny.web.programs.LinksProgram
import com.ntny.web.api.version
import org.http4s.HttpApp
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder

object Application extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    val router = Router(
      version.v1 -> LinksProgram.makeIO().routes
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
