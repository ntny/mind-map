package com.ntny.web.features.links

import cats.effect.BracketThrow
import cats.{Defer, Monad, MonadThrow}
import com.ntny.dba.links.commands.PutLinkCommand
import com.ntny.dba.links.queries.{CategoriesQuery, CategoryLinksQuery}
import com.ntny.web.decoder._
import com.ntny.web.features.links.LinksRoutes.ownerParam
import com.ntny.web.features.links.LinksRoutes.categoryParam
import com.ntny.web.features.links.models.{ValidatedCategory, ValidatedNewLink, ValidatedOwner}
import com.ntny.web.json._
import com.ntny.web.query._
import doobie.hikari.HikariTransactor
import doobie.implicits._
import org.http4s.{HttpRoutes}
import org.http4s.circe.JsonDecoder
import org.http4s.dsl.Http4sDsl
import com.ntny.web.features.links.models.ConverterSyntax._

class LinksRoutes[F[_] : Defer : Monad : BracketThrow : JsonDecoder : MonadThrow]
(
  transactor: HikariTransactor[F]
) extends Http4sDsl[F] {

  def routes: HttpRoutes[F] = HttpRoutes.of[F] {

    case GET -> Root / "categories" :? ownerParam(owner) =>
      Ok(CategoriesQuery(owner.toDomain).transact(transactor))
    case GET -> Root / "links" :? ownerParam(owner) +& categoryParam(category) => {
      Ok(CategoryLinksQuery((owner, category).toDomain).transact(transactor))
    }

    case req @ PUT -> Root / "links" =>
      req.decodeR[ValidatedNewLink] { link: ValidatedNewLink =>
        Ok(PutLinkCommand(link.toDomain).transact(transactor))
      }
  }
}

object LinksRoutes{
  import org.http4s.dsl.impl.QueryParamDecoderMatcher

  object ownerParam extends QueryParamDecoderMatcher[ValidatedOwner]("owner")
  object categoryParam extends QueryParamDecoderMatcher[ValidatedCategory]("category")
}
