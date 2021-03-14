package com.ntny.web.features.categories

import cats.{Defer, Monad, MonadThrow}
import cats.effect.BracketThrow
import com.ntny.dba.categories.commands.PutCategory
import com.ntny.dba.categories.queries.CategoriesQuery
import com.ntny.web.features.authentification.input.AuthenticatedUser
import com.ntny.web.features.categories.input.ValidatedCategoryName
import doobie.hikari.HikariTransactor
import org.http4s.{AuthedRoutes, HttpRoutes}
import org.http4s.circe.JsonDecoder
import org.http4s.dsl.Http4sDsl

class CategoriesRoutes[F[_] : Defer : Monad : BracketThrow : JsonDecoder : MonadThrow]
(
  transactor: HikariTransactor[F]
) extends Http4sDsl[F] {

  import doobie.implicits._

  import com.ntny.web.codecs.decoder._
  import com.ntny.web.codecs.json._

  import com.ntny.web.features.links.input.ConverterSyntax._

  def routes: AuthedRoutes[AuthenticatedUser, F] = AuthedRoutes.of {
    case GET -> Root / "categories" as user =>
      Ok(CategoriesQuery(user.toDomain).transact(transactor))

    case cat @  PUT -> Root / "categories" as user  =>
      cat.req.decodeR[ValidatedCategoryName]{ category =>
        Ok(PutCategory(user.toDomain, category.toDomain).transact(transactor))
      }
  }
}
