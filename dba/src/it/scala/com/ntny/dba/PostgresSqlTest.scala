package com.ntny.dba

import cats.effect.{Blocker, IO}
import com.dimafeng.testcontainers.{ForAllTestContainer, PostgreSQLContainer}
import doobie.util.ExecutionContexts
import doobie.util.transactor.Transactor
import doobie.util.transactor.Transactor.Aux
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.output.MigrateResult
import org.scalatest.Suite

import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext

trait PostgresSqlTest extends ForAllTestContainer { self: Suite =>
  def container: PostgreSQLContainer

  private implicit val cs = IO.contextShift(ExecutionContext.global)
  lazy val xa: Aux[IO, Unit] = PostgresSqlTransactor(container)
}

private object PostgresSqlTransactor {
  private implicit val cs = IO.contextShift(ExecutionContexts.synchronous)

  private def applyMigrations(container: PostgreSQLContainer): MigrateResult = {
    Flyway
      .configure()
      .locations("db/migrations")
      .dataSource(container.jdbcUrl, container.username, container.password)
      .load()
      .migrate()
  }

  def apply(container: PostgreSQLContainer): Aux[IO, Unit] = {
    applyMigrations(container)
    Transactor.fromDriverManager[IO](
      container.container.getDriverClassName,
      container.container.getJdbcUrl,
      container.username,
      container.password,
      Blocker.liftExecutionContext(ExecutionContext.fromExecutor(Executors.newFixedThreadPool(3)))
    )
  }
}
