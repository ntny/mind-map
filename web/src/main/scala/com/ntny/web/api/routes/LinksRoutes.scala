package com.ntny.web.api.routes

import cats.{Defer, Monad, MonadThrow}
import com.ntny.dba.LinksStorage
import com.ntny.models.Link
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import com.ntny.web.json._
import com.ntny.web.decoder._
import org.http4s.circe.JsonDecoder

class LinksRoutes[F[_]: Defer: Monad: LinksStorage: JsonDecoder: MonadThrow]
  extends Http4sDsl[F] {
  private val storage = implicitly[LinksStorage[F]]

  def routes: HttpRoutes[F] = HttpRoutes.of[F]{
    case GET -> Root / "links"   => Ok(storage.all)
    case req @ PUT -> Root / "links" =>
      req.decodeR[Link]{link: Link =>
        Ok(storage.put(link))
      }
  }
}
