package com.ntny.web

import com.ntny.dba.links.queries.output.Category
import com.ntny.dba.links.queries.output.Link
import com.ntny.web.features.links.input.{ValidatedCategory, ValidatedNewLink}
import io.circe.{Decoder, Encoder}
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

object json extends JsonCodecs {
  implicit def deriveEntityEncoder[F[_], A: Encoder]: EntityEncoder[F, A] = jsonEncoderOf[F, A]
}

private[web] class JsonCodecs {
  import io.circe.refined._
  import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
  import eu.timepit.refined.types.AllTypes

  implicit val linkDecoder: Decoder[ValidatedNewLink] = deriveDecoder[ValidatedNewLink]

  implicit val linkEncoder: Encoder[Link] = deriveEncoder[Link]
  implicit val categoryEncoder: Encoder[Category] = deriveEncoder[Category]
}
