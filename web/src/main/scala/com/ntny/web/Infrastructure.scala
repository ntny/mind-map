package com.ntny.web

import cats.effect.{Blocker, IO, Resource}
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts

object Infrastructure {
  implicit val cs = IO.contextShift(ExecutionContexts.synchronous)
  def transactor(): Resource[IO, HikariTransactor[IO]] = {
      for {
        ce <- ExecutionContexts.fixedThreadPool[IO](32) // our connect EC
        be <- Blocker[IO]
        xa: HikariTransactor[IO] <- HikariTransactor.newHikariTransactor[IO](
          "org.postgresql.Driver",
          "jdbc:postgresql://localhost:5432/postgres",
          "postgres",
          "password123",
          ce,
          be
        )
      } yield xa
  }
}
