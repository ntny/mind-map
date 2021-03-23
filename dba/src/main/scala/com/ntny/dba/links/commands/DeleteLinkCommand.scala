package com.ntny.dba.links.commands

import com.ntny.dba.AuthenticatedOwner
import doobie.free.connection.ConnectionIO
import doobie.implicits.toSqlInterpolator

class DeleteLinkCommand(owner: AuthenticatedOwner, link: String) {
  def exec(): ConnectionIO[Int] = {
    sql"""
          DELETE FROM links WHERE url = $link
       """.update.run
  }
}

object DeleteLinkCommand {
  def apply(owner: AuthenticatedOwner, link: String): ConnectionIO[Int] = new DeleteLinkCommand(owner, link).exec()
}
