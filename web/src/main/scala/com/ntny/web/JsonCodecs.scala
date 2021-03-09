package com.ntny.web

import com.ntny.dba.links.Link
import com.ntny.web.features.links.models.ValidatedLink
import io.circe.{Decoder, Encoder}
import org.http4s.EntityEncoder
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
