package com.ntny.dba.links.queries

import com.ntny.dba.Query
import com.ntny.dba.links.queries.input.Owner
import com.ntny.dba.links.queries.output.Category
import doobie.ConnectionIO
import com.ntny.dba.codecs.readers._
import doobie.implicits.toSqlInterpolator

class CategoriesQuery(owner: Owner) extends Query[ConnectionIO, List[Category]] {
  override def exec(): ConnectionIO[List[Category]] = {
    sql"""
        SELECT name
         FROM categories
         WHERE owner_id = ${owner.id.toString}::uuid
         ORDER BY created DESC
       """.query[Category].to[List]
  }
}

object  CategoriesQuery{
  def apply(owner: Owner): ConnectionIO[List[Category]] = new CategoriesQuery(owner).exec()
}


