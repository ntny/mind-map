package com.ntny.dba.links.queries

import com.ntny.dba.{AuthenticatedOwner, CategoryId, Query}
import com.ntny.dba.links.queries.output.Link
import doobie.ConnectionIO
import com.ntny.dba.codecs.readers._
import doobie.implicits.toSqlInterpolator

class CategoryLinksQuery(owner: AuthenticatedOwner, categoryId: CategoryId) extends Query[ConnectionIO, List[Link]]{
  override def exec(): ConnectionIO[List[Link]] = {
    sql"""
        SELECT
          url
          , name
          , description
          , deadline
         FROM Links
         WHERE owner_id = ${owner.id.toString}::uuid
         AND category_id = ${categoryId.id.toString}::uuid
         ORDER BY created DESC
       """.query[Link].to[List]
  }
}

object CategoryLinksQuery{
  def apply(owner: AuthenticatedOwner, categoryId: CategoryId): ConnectionIO[List[Link]] = new CategoryLinksQuery(owner, categoryId).exec()
}
