package com.ntny.web.features.preview

import java.util.UUID

import cats.{Defer, Monad, MonadThrow}
import cats.effect.BracketThrow
import com.ntny.dba.categories.output.CategoryLink
import com.ntny.dba.categories.queries.CategoryLinkQuery
import com.ntny.web.features.preview.output.{CategoryPreview, LinkPreview}
import com.ntny.web.middleware.authentication.output.AuthenticatedUser
import doobie.hikari.HikariTransactor
import org.http4s.AuthedRoutes
import org.http4s.circe.JsonDecoder
import org.http4s.dsl.Http4sDsl

class PreviewRoutes [F[_] : Defer : Monad : BracketThrow : JsonDecoder : MonadThrow]
(
  transactor: HikariTransactor[F]
) extends Http4sDsl[F]  {

  import doobie.implicits._
  import cats.implicits._

  import com.ntny.web.codecs.json._

  import com.ntny.web.features.links.input.ConverterSyntax._

  val routes: AuthedRoutes[AuthenticatedUser, F] = AuthedRoutes.of {
    case GET -> Root / "preview" as user =>
      Ok {
        for {
          categoryLinks <- CategoryLinkQuery(user.toDomain).transact(transactor)
          categories: Map[(UUID, String), List[CategoryLink]] = categoryLinks.groupBy(c => c.categoryId -> c.categoryName)
        } yield categories.map {
          case ((categoryId, categoryName), links) =>
            CategoryPreview(categoryId, categoryName, links.map(l => LinkPreview(l.linkName, l.linkUrl)))
        }
      }
  }
}
