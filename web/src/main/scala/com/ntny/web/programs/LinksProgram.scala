package com.ntny.web.programs

import java.time.Instant

import cats.effect.IO
import com.ntny.dba.LinksStorage
import com.ntny.models
import com.ntny.models.Link
import com.ntny.web.api.routes.LinksRoutes
import eu.timepit.refined.auto._

object LinksProgram {
  def makeIO(): LinksRoutes[IO] = {
    implicit val storage: LinksStorage[IO] = new LinksStorage[IO] {
      override def put(link: Link): IO[Unit] = {
        IO(println(s"${link.name} is added"))
      }

      override def all: IO[List[Link]] = IO.pure(List(
        models.Link(
          "https://patternsinfp.wordpress.com/2011/01/31/lenses-are-the-coalgebras-for-the-costate-comonad/"
          , "Lenses are the coalgebras for the costate comonad"
          , None
          , Instant.now()
        )
      ))
    }
    new LinksRoutes[IO]()
  }
}
