package com.ntny.web

import eu.timepit.refined.api.Refined
import eu.timepit.refined.boolean.And
import eu.timepit.refined.numeric.{Greater, Less}
import eu.timepit.refined.types.string.NonEmptyString

object Config {
  import eu.timepit.refined.auto._

  val jdbcUrl: JdbcUrl = JdbcUrl("localhost:5432/postgres")
  val databaseUserName: DbUserName = DbUserName("postgres")
  val databasePassword: DbPassword = DbPassword("password123")

  val apiPort: PortNumber = 8080
  val apiHost: NonEmptyString = "localhost"


  case class JdbcUrl(url: NonEmptyString)
  case class DbUserName(name: NonEmptyString)
  case class DbPassword(value: NonEmptyString)

  type PortNumber = Int Refined And[Greater[8000], Less[50000]]
}
