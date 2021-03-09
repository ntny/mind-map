package com.ntny.dba

import java.time.LocalDateTime

import com.ntny.dba.links.Link
import doobie.util.Read
import doobie.implicits._
import doobie.implicits.javatime._

object codecs {
  object readers {
    implicit val linkWriter: Read[Link] = Read[
      (
        String
          , String
          , Option[String]
          , LocalDateTime
        )
    ].map{
      case (url, name, description, deadline) => Link(url, name, description, deadline)
    }
  }
}
