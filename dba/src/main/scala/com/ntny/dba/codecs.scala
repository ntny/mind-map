package com.ntny.dba

import com.ntny.dba.links.queries.output
import com.ntny.dba.links.queries.output.{Category, Link}

import java.time.LocalDateTime
import doobie.util.Read
import doobie.implicits._
import doobie.implicits.javatime._

object codecs {
  object readers {
    implicit val link: Read[Link] = Read[
      (
        String
          , String
          , Option[String]
          , LocalDateTime
        )
    ].map{
      case (url, name, description, deadline) => output.Link(url, name, description, deadline)
    }
    implicit val category: Read[Category] = Read[
      (
        String
        )
    ].map{
      case (name) => output.Category(name)
    }
  }
}
