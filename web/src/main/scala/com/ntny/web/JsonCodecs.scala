package com.ntny.web

import java.util.UUID

import com.ntny.dba.links.Link
import com.ntny.web.features.links.models.{ValidatedLink, ValidatedOwner}
import io.circe.{Decoder, Encoder}
import org.http4s.{EntityEncoder, QueryParamDecoder}
import org.http4s.circe.jsonEncoderOf

object json extends JsonCodecs {
  implicit def deriveEntityEncoder[F[_], A: Encoder]: EntityEncoder[F, A] = jsonEncoderOf[F, A]
}

private[web] class JsonCodecs {
  import io.circe.refined._
  import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

  implicit val linkDecoder: Decoder[ValidatedLink] = deriveDecoder[ValidatedLink]
  implicit val linkEncoder: Encoder[Link] = deriveEncoder[Link]
}
