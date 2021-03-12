package com.ntny.dba.categories.queries

import com.ntny.dba.categories.output.Category
import com.ntny.dba.codecs.readers._
import com.ntny.dba.{Owner, Query}
import doobie.ConnectionIO
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


