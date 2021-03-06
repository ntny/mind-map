package com.ntny.dba.links.commands

import com.ntny.dba.{AuthenticatedOwner, Command}
import com.ntny.dba.links.commands.input.NewLink
import doobie.free.connection.ConnectionIO
import doobie.implicits._
import doobie.implicits.javatime._

class PutLinkCommand(owner: AuthenticatedOwner, link: NewLink) extends Command[ConnectionIO] {
  override def exec(): ConnectionIO[Int] = {
    sql"""
         INSERT INTO Links (
                   owner_id
                   , category_id
                   , url
                   , name
                   , description
                   , deadline
                 )
                 VALUES (
                  ${owner.id.toString}::uuid
                  , ${link.categoryId.toString}::uuid
                  , ${link.url}
                  , ${link.name}
                  , ${link.description}
                  , ${link.deadline}
                 )
       """.update.run
  }
}

object PutLinkCommand{
  def apply(owner: AuthenticatedOwner, link: NewLink): ConnectionIO[Int] = new PutLinkCommand(owner, link).exec()
}