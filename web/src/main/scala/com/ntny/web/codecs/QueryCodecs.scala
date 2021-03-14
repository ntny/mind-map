package com.ntny.web.codecs

import com.ntny.dba.AuthenticatedOwner
import com.ntny.web.middleware.authentication.output.AuthenticatedUser
import com.ntny.web.features.links.input
import com.ntny.web.features.links.input.ValidatedCategoryId
import eu.timepit.refined.string.Uuid
import org.http4s.{ParseFailure, QueryParamDecoder}

import java.util.UUID

object query extends QueryCodecs {}

private[web] class QueryCodecs {

  import cats.syntax.all._
  import eu.timepit.refined._

  implicit val ownerQueryParamDecoder: QueryParamDecoder[AuthenticatedUser] =
    QueryParamDecoder[String]
      .emap(id => refineV[Uuid](id).leftMap(m => ParseFailure(m,m)))
      .map(id => AuthenticatedUser(id))

  implicit val categoryQueryParamDecoder: QueryParamDecoder[ValidatedCategoryId] =
    QueryParamDecoder[String]
      .emap(id => refineV[Uuid](id).leftMap(m => ParseFailure(m,m)))
      .map(id => input.ValidatedCategoryId(id))


  import org.http4s.dsl.impl.QueryParamDecoderMatcher
  object categoryParam extends QueryParamDecoderMatcher[ValidatedCategoryId]("category")
  object ownerParam extends QueryParamDecoderMatcher[AuthenticatedUser]("owner")
}
