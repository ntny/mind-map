package com.ntny.web.features.categories

import cats.{Defer, Monad, MonadThrow}
import cats.effect.BracketThrow
import com.ntny.dba.categories.queries.CategoriesQuery
import com.ntny.web.codecs.queryMatcher._
import com.ntny.web.codecs.json._
import doobie.implicits._
import doobie.hikari.HikariTransactor
import org.http4s.HttpRoutes
import org.http4s.circe.JsonDecoder
import org.http4s.dsl.Http4sDsl

class CategoriesRoutes[F[_] : Defer : Monad : BracketThrow : JsonDecoder : MonadThrow]
(
  transactor: HikariTransactor[F]
) extends Http4sDsl[F] {
  import com.ntny.web.features.links.input.ConverterSyntax._

  def routes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / "categories" :? ownerParam(owner) =>
      Ok(CategoriesQuery(owner.toDomain).transact(transactor))
  }
}
