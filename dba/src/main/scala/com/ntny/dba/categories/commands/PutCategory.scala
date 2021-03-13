package com.ntny.dba.categories.commands

import com.ntny.dba.categories.commands.input.NewCategory
import doobie.ConnectionIO
import doobie.implicits.toSqlInterpolator

import java.util.UUID

class PutCategory(input: NewCategory) {
  def exec(): ConnectionIO[UUID] = {
    val categoryId = UUID.randomUUID()
    sql"""
         INSERT INTO categories (
                   category_id
                   , owner_id
                   , name
                 )
                 VALUES (
                  ${categoryId.toString}::uuid
                  , ${input.ownerId.id.toString}::uuid
                  , ${input.name.name}
                 )
       """.update.run.map(_ => categoryId)
  }
}

object PutCategory {
  def apply(input: NewCategory): ConnectionIO[UUID] = new PutCategory(input).exec()
}
