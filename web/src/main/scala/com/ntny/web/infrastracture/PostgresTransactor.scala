package com.ntny.web.infrastracture

import cats.effect.{Async, Blocker, ContextShift, Resource}
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
}
