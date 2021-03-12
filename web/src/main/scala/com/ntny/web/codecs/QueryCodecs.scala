package com.ntny.web.codecs

import com.ntny.web.features.links.input.{ValidatedCategory, ValidatedOwner}
import eu.timepit.refined.string.Uuid
import org.http4s.{ParseFailure, QueryParamDecoder}

object query extends QueryCodecs {}

private[web] class QueryCodecs {

  import cats.syntax.all._
  import eu.timepit.refined._

  implicit val ownerQueryParamDecoder: QueryParamDecoder[ValidatedOwner] =
    QueryParamDecoder[String]
      .emap(id => refineV[Uuid](id).leftMap(m => ParseFailure(m,m)))
      .map(id => ValidatedOwner(id))

  implicit val categoryQueryParamDecoder: QueryParamDecoder[ValidatedCategory] =
    QueryParamDecoder[String]
      .emap(id => refineV[Uuid](id).leftMap(m => ParseFailure(m,m)))
      .map(id => ValidatedCategory(id))


  import org.http4s.dsl.impl.QueryParamDecoderMatcher
  object categoryParam extends QueryParamDecoderMatcher[ValidatedCategory]("category")
  object ownerParam extends QueryParamDecoderMatcher[ValidatedOwner]("owner")
}
