package com.ntny.web.features.links

import cats.effect.BracketThrow
import cats.{Defer, Monad, MonadThrow}
import com.ntny.dba.links.commands.PutLinkCommand
import com.ntny.dba.links.queries.CategoryLinksQuery
import com.ntny.web.features.authentification.input.AuthenticatedUser
import com.ntny.web.features.links.input.ValidatedNewLink
import doobie.hikari.HikariTransactor
import org.http4s.{AuthedRoutes, HttpRoutes}
import org.http4s.circe.JsonDecoder
import org.http4s.dsl.Http4sDsl

class LinksRoutes[F[_] : Defer : Monad : BracketThrow : JsonDecoder : MonadThrow]
(
  transactor: HikariTransactor[F]
) extends Http4sDsl[F] {

  import doobie.implicits._


  import com.ntny.web.codecs.decoder._
  import com.ntny.web.codecs.json._
  import com.ntny.web.codecs.query._

  import com.ntny.web.features.links.input.ConverterSyntax._

  def routes: AuthedRoutes[AuthenticatedUser, F] = AuthedRoutes.of {

    case GET -> Root / "links" :? categoryParam(category) as user => {
      Ok(CategoryLinksQuery(user.toDomain, category.toDomain).transact(transactor))
    }
    case l @ PUT -> Root / "links" as user =>
      l.req.decodeR[ValidatedNewLink] { link: ValidatedNewLink =>
        Ok(PutLinkCommand(user.toDomain, link.toDomain).transact(transactor))
      }
  }
}
