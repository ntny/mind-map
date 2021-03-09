package com.ntny.dba.links.queries

import com.ntny.dba.Query
import doobie.implicits._
import com.ntny.dba.codecs.readers._
import com.ntny.dba.links.Link
import com.ntny.dba.links.queries.inputs.Owner
import doobie.free.connection.ConnectionIO

class OwnerLinksQuery(owner: Owner) extends Query[ConnectionIO, List[Link]] {

  override def exec(): ConnectionIO[List[Link]] = {
    sql"""
        SELECT
          url
          , name
          , description
          , deadline
         FROM Links
         WHERE owner_id = ${owner.id.toString}::uuid
       """.query[Link].to[List]
  }
}

object OwnerLinksQuery{
  def apply(owner: Owner): ConnectionIO[List[Link]] = new OwnerLinksQuery(owner).exec()
}
