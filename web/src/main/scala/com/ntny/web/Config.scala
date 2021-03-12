package com.ntny.web

import eu.timepit.refined.types.string.NonEmptyString

object Config {
  import eu.timepit.refined.auto._

  val jdbcUrl: JdbcUrl = JdbcUrl("localhost:5432/postgres")
  val databaseUserName: DbUserName = DbUserName("postgres")
  val databasePassword: DbPassword = DbPassword("password123")

  case class JdbcUrl(url: NonEmptyString)
  case class DbUserName(name: NonEmptyString)
  case class DbPassword(value: NonEmptyString)
}
