package com.ntny.dba.links.commands

import com.ntny.dba.Command
import com.ntny.dba.links.rows.NewLink
import doobie.free.connection.ConnectionIO
import doobie.implicits._
import doobie.implicits.javatime._

class PutLinkCommand(link: NewLink) extends Command[ConnectionIO] {
  override def exec(): ConnectionIO[Int] = {
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

object PutLinkCommand{
  def apply(link: NewLink): ConnectionIO[Int] = new PutLinkCommand(link).exec()
}