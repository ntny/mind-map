package com.ntny.dba.links.queries

import com.ntny.dba.Query
import doobie.implicits._
import com.ntny.dba.codecs.readers._
import com.ntny.dba.links.Link
import doobie.free.connection.ConnectionIO

class OwnerLinksQuery(owner: String) extends Query[ConnectionIO, List[Link]] {

  override def exec(): ConnectionIO[List[Link]] = {
    sql"""
        SELECT
          url
          , name
          , description
          , deadline
         FROM Links
         WHERE owner_id = $owner::uuid
       """.query[Link].to[List]
  }
}

object OwnerLinksQuery{
  def apply(owner: String): ConnectionIO[List[Link]] = new OwnerLinksQuery(owner).exec()
}
