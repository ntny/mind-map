package com.ntny.dba.links.queries

import com.ntny.dba.Query
import com.ntny.dba.links.queries.input.CategoryLinkParams
import com.ntny.dba.links.queries.output.Link
import doobie.ConnectionIO
import com.ntny.dba.codecs.readers._
import doobie.implicits.toSqlInterpolator

class CategoryLinksQuery(query: CategoryLinkParams) extends Query[ConnectionIO, List[Link]]{
  override def exec(): ConnectionIO[List[Link]] = {
    sql"""
        SELECT
          url
          , name
          , description
          , deadline
         FROM Links
         WHERE owner_id = ${query.owner.id.toString}::uuid
         AND category_id = ${query.category.id.toString}::uuid
         ORDER BY created DESC
       """.query[Link].to[List]
  }
}

object CategoryLinksQuery{
  def apply(query: CategoryLinkParams): ConnectionIO[List[Link]] = new CategoryLinksQuery(query).exec()
}
