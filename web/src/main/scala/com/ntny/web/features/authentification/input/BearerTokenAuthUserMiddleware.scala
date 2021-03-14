package com.ntny.web.features.authentification.input

import cats.Monad
import cats.data.{EitherT, Kleisli, OptionT}
import org.http4s.Credentials.Token
import org.http4s.dsl.Http4sDsl
import org.http4s.{AuthScheme, AuthedRoutes, Request}
import org.http4s.headers.Authorization
import org.http4s.server.AuthMiddleware

object BearerTokenAuthUserMiddleware {
  import cats.implicits._

  def apply[F[_]: Monad](authenticate: String => F[Option[AuthenticatedUser]]): AuthMiddleware[F, AuthenticatedUser] = {

    val dsl = new Http4sDsl[F] {}; import dsl._

    val onFailure: AuthedRoutes[String, F] =
      Kleisli(req => OptionT.liftF(Forbidden(req.context)))

    def authUser: Kleisli[F, Request[F], Either[String, AuthenticatedUser]] = {
      Kleisli{
        request =>
          val authenticated = for {
            token <- EitherT.fromEither[F](request.bearerToken.fold(notFoundBearerToken)(_.asRight[String]))
            user <- EitherT(authenticate(token).map(_.fold(unrecognizedBearerToken)(_.asRight[String])))
          } yield user
          authenticated.fold(l => l.asLeft[AuthenticatedUser], r => r.asRight[String])
         }
    }
    AuthMiddleware(authUser, onFailure)
  }

  val notFoundBearerToken: Either[String, String] = "Not found authentication data".asLeft[String]
  val unrecognizedBearerToken: Either[String, AuthenticatedUser] = "Unrecognized authentication data".asLeft[AuthenticatedUser]


  implicit class RequestOps[F[_]](r: Request[F]) {
    def bearerToken: Option[String] = r.headers.get(Authorization).collect {
      case Authorization(Token(AuthScheme.Bearer, token)) => token
    }
  }
}
