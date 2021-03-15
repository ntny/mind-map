package com.ntny.dba.categories.queries

import com.ntny.dba.AuthenticatedOwner
import com.ntny.dba.categories.output.CategoryLink
import doobie.ConnectionIO

class CategoryLinkQuery(owner: AuthenticatedOwner) {
  import com.ntny.dba.codecs.readers._
  import doobie.implicits._
  def exec(): ConnectionIO[List[CategoryLink]] = {
    sql"""
        SELECT c.name, c.category_id, l.url, l.name
        FROM categories as c
        INNER JOIN links as l
        ON l.category_id = c.category_id AND l.owner_id = c.owner_id
        WHERE c.owner_id = ${owner.id.toString}::uuid
        ORDER BY l.created DESC
       """.query[CategoryLink].to[List]
  }
}

object CategoryLinkQuery {
  def apply(owner: AuthenticatedOwner): ConnectionIO[List[CategoryLink]] = new CategoryLinkQuery(owner).exec()
}
