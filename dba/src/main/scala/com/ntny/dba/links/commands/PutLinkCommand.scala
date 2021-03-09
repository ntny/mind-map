package com.ntny.dba.links.commands

import com.ntny.dba.Command
import com.ntny.dba.links.rows.NewLink
import doobie.free.connection.ConnectionIO
import doobie.implicits._
import doobie.implicits.javatime._

class PutLinkCommand extends Command[ConnectionIO, NewLink] {
  override def exec(link: NewLink): ConnectionIO[Int] = {
    sql"""
         INSERT INTO Links (
                   owner_id
                   , url
                   , name
                   , description
                   , deadline
                 )
                 VALUES(
                  ${link.ownerId.toString}::uuid
                  , ${link.url}
                  , ${link.name}
                  , ${link.description}
                  , ${link.deadline}
                 )
       """.update.run
  }
}
