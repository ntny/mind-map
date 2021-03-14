package com.ntny.dba.categories.commands

import com.ntny.dba.{AuthenticatedOwner, NewCategoryName}
import doobie.ConnectionIO
import doobie.implicits.toSqlInterpolator

import java.util.UUID

class PutCategory(owner: AuthenticatedOwner, categoryName: NewCategoryName) {
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
                  , ${owner.id.toString}::uuid
                  , ${categoryName.name}
                 )
       """.update.run.map(_ => categoryId)
  }
}

object PutCategory {
  def apply(owner: AuthenticatedOwner, categoryName: NewCategoryName): ConnectionIO[UUID] = new PutCategory(owner, categoryName).exec()
}
