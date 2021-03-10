package com.ntny.web

import cats.MonadThrow
import cats.syntax.all._
import io.circe.Decoder
import org.http4s._
import org.http4s.dsl.Http4sDsl
import org.http4s.circe._

object decoder {

  implicit class RefinedRequestDecoder[F[_]: JsonDecoder: MonadThrow](req: Request[F]) extends Http4sDsl[F] {

    def decodeR[A: Decoder](f: A => F[Response[F]]): F[Response[F]] =
      req.asJsonDecode[A].attempt.flatMap {
        case Left(e: MalformedMessageBodyFailure) => UnprocessableEntity(e.message)
        case Left(e: MessageBodyFailure) =>
          Option(e.getCause) match {
            case Some(c)  => BadRequest(c.getMessage)
            case _ => UnprocessableEntity()
          }
        case Right(a) => f(a)
      }
  }

}
