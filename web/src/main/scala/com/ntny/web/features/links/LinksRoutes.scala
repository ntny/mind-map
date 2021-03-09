package com.ntny.web.features.links

import cats.data.{NonEmptyList, ValidatedNel}
import cats.effect.BracketThrow
import cats.{Defer, Monad, MonadThrow}
import com.ntny.dba.links.commands.PutLinkCommand
import com.ntny.dba.links.queries.OwnerLinksQuery
import com.ntny.web.decoder._
import com.ntny.web.features.links.models.{ValidatedLink, ValidatedOwner}
import com.ntny.web.json._
import doobie.hikari.HikariTransactor
import doobie.implicits._
import org.http4s.{HttpRoutes, ParseFailure}
import org.http4s.circe.JsonDecoder
import org.http4s.dsl.Http4sDsl
import com.ntny.web.features.links.models.ConverterSyntax._

class LinksRoutes[F[_] : Defer : Monad : BracketThrow : JsonDecoder : MonadThrow]
(
  transactor: HikariTransactor[F]
) extends Http4sDsl[F] {

  object ownerParam extends ValidatingQueryParamDecoderMatcher[ValidatedOwner]("owner")

  def routes: HttpRoutes[F] = HttpRoutes.of[F] {

    case GET -> Root / "links" :? ownerParam(request: ValidatedNel[ParseFailure, ValidatedOwner]) =>
      request.fold(
        _ => BadRequest("unable to parse parameter owner"),
        owner => Ok(OwnerLinksQuery(owner.toDbo).transact(transactor))
      )
    case req@PUT -> Root / "links" =>
      req.decodeR[ValidatedLink] { link: ValidatedLink =>
        Ok(PutLinkCommand(link.toDbo).transact(transactor))
      }
  }
}
