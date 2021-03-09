package com.ntny.web.features.links

import cats.effect.BracketThrow
import cats.{Defer, Monad, MonadThrow}
import com.ntny.dba.links.commands.PutLinkCommand
import com.ntny.dba.links.queries.OwnerLinksQuery
import com.ntny.web.decoder._
import com.ntny.web.features.links.models.ValidatedLink
import com.ntny.web.json._
import doobie.hikari.HikariTransactor
import doobie.implicits._
import org.http4s.HttpRoutes
import org.http4s.circe.JsonDecoder
import org.http4s.dsl.Http4sDsl
import com.ntny.web.features.links.models.ConverterSyntax._

class LinksRoutes[F[_]: Defer: Monad: BracketThrow: JsonDecoder: MonadThrow]
(
  transactor: HikariTransactor[F]
  , putLinkCommand: PutLinkCommand
  , ownerLinksQuery: OwnerLinksQuery
) extends Http4sDsl[F] {

  object ownerParam extends QueryParamDecoderMatcher[String]("owner")

  def routes: HttpRoutes[F] = HttpRoutes.of[F]{
    case GET -> Root / "links" :? ownerParam(owner) =>
      val response = ownerLinksQuery.exec(owner).transact(transactor)
      Ok(response)
    case req @ PUT -> Root / "links" =>
      req.decodeR[ValidatedLink]{ link: ValidatedLink =>
        Ok(putLinkCommand.exec(link.toDbo).transact(transactor))
      }
  }
}
