import cats.effect.{Blocker, ExitCode, IO, IOApp}
import com.codahale.metrics.ConsoleReporter
import com.ntny.web.features.categories.CategoriesRoutes
import com.ntny.web.features.links.LinksRoutes
import com.ntny.web.features.preview.PreviewRoutes
import com.ntny.web.infrastracture.PostgresTransactor
import com.ntny.web.middleware.authentication.BearerTokenAuthMiddleware
import com.ntny.web.middleware.authentication.output.AuthenticatedUser
import com.ntny.web.middleware.metrics.MetricsMiddleware
import com.ntny.web.{Config, version}
import eu.timepit.refined.string.Uuid
import org.http4s.{HttpApp, HttpRoutes}
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder

import java.io.File
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit
import scala.concurrent.ExecutionContext
import scala.util.Try

object Application extends IOApp {

  def authenticateMock(token: String): IO[Option[AuthenticatedUser]] = {
    import eu.timepit.refined.refineV

    import java.util.Base64

    val decoded = Try {
      Base64.getDecoder.decode(token.getBytes(StandardCharsets.UTF_8))
    }.map(new String(_))
      .map(refineV[Uuid](_).map(AuthenticatedUser).toOption)
    IO.pure(decoded.fold(_ => None, s => s))
  }

  val authMiddleware = BearerTokenAuthMiddleware[IO](authenticateMock)

  import com.codahale.metrics.CsvReporter
  import java.util.Locale
  import java.util.concurrent.TimeUnit

  val reporter: CsvReporter = CsvReporter
    .forRegistry(MetricsMiddleware.appRegistry)
    .formatFor(Locale.US).convertRatesTo(TimeUnit.SECONDS)
    .convertDurationsTo(TimeUnit.MILLISECONDS).build(new File("./metrics"))
  reporter.start(1, TimeUnit.SECONDS)

  override def run(args: List[String]): IO[ExitCode] = {
    val blocker: Blocker = Blocker.liftExecutionContext(ExecutionContext.global)
    PostgresTransactor[IO](Config.jdbcUrl, Config.databaseUserName, Config.databasePassword)(blocker).use { xa =>
      val router: HttpRoutes[IO] =  Router(
        version.v1 -> authMiddleware(new LinksRoutes[IO](xa).routes),
        version.v1 -> authMiddleware(new CategoriesRoutes[IO](xa).routes),
        version.v1 -> authMiddleware(new PreviewRoutes[IO](xa).routes)
      )

      import org.http4s.implicits._
      val httpApp: HttpApp[IO] = MetricsMiddleware(router).orNotFound

      import scala.concurrent.ExecutionContext.Implicits.global
      val serverBuilder = BlazeServerBuilder[IO](global).bindHttp(Config.apiPort.value, Config.apiHost.value).withHttpApp(httpApp)
      serverBuilder
        .serve
        .compile
        .drain
        .map(_ => ExitCode.Success)
    }
  }
}
