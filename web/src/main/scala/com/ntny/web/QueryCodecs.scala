package com.ntny.web

import java.util.UUID

import com.ntny.web.features.links.models.ValidatedOwner
import org.http4s.QueryParamDecoder

object query extends QueryCodecs{}

private [web] class QueryCodecs {
  implicit val ownerQueryParamDecoder: QueryParamDecoder[ValidatedOwner] =
    QueryParamDecoder[String]
      .map(id => ValidatedOwner(UUID.fromString(id)))
}
