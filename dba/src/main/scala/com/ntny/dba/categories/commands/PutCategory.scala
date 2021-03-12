package com.ntny.dba.categories.commands

import com.ntny.dba.Command
import com.ntny.dba.categories.commands.input.NewCategory
import doobie.ConnectionIO
import doobie.implicits.toSqlInterpolator

import java.util.UUID

class PutCategory(input: NewCategory) extends Command[ConnectionIO]{
  override def exec(): ConnectionIO[Int] = {
    sql"""
         INSERT INTO categories (
                   category_id
                   , owner_id
                   , name
                 )
                 VALUES (
                  ${UUID.randomUUID().toString}::uuid
                  , ${input.ownerId.id.toString}::uuid
                  , ${input.name.name}
                 )
       """.update.run
  }
}

object PutCategory {
  def apply(input: NewCategory): ConnectionIO[Int] = new PutCategory(input).exec()
}
