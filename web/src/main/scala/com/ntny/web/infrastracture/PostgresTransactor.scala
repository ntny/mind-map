package com.ntny.web.infrastracture

import cats.effect.{Async, Blocker, ContextShift, IO, Resource}
import com.ntny.web.Config.{DbPassword, DbUserName, JdbcUrl}
import com.zaxxer.hikari.HikariConfig
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts

object PostgresTransactor {
  private val jdbcDriver: String = "org.postgresql.Driver"
  private val protocol: String = "jdbc:postgresql"

  def hikariConfig(url: JdbcUrl, username: DbUserName, pass: DbPassword): HikariConfig = {
    val config = new HikariConfig()
    config.setDriverClassName(jdbcDriver)
    config.setJdbcUrl(s"$protocol://${url.url.value}")
    config.setUsername(username.name.value)
    config.setPassword(pass.value.value)
    config
  }

  def apply[F[_]: Async: ContextShift](url: JdbcUrl, username: DbUserName, pass: DbPassword)(blocker: Blocker): Resource[F, HikariTransactor[F]] = {
    val config = hikariConfig(url, username, pass)
    for {
      ce <- ExecutionContexts.fixedThreadPool[F](3)
      xa <- HikariTransactor.fromHikariConfig[F](config, ce, blocker)
    } yield xa
  }

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
