package com.ntny.web
import com.ntny.web.query._
import com.ntny.web.features.links.input.{ValidatedCategory, ValidatedOwner}

object queryMatcher extends QueryMatcherCodecs

private[web] class QueryMatcherCodecs {
  import org.http4s.dsl.impl.QueryParamDecoderMatcher
  object categoryParam extends QueryParamDecoderMatcher[ValidatedCategory]("category")
  object ownerParam extends QueryParamDecoderMatcher[ValidatedOwner]("owner")
}
