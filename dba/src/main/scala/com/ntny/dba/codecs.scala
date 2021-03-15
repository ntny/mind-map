package com.ntny.dba

import com.ntny.dba.categories.output.CategoryLink
import com.ntny.dba.links.queries.output
import com.ntny.dba.links.queries.output.Link

import java.time.LocalDateTime
import doobie.util.Read
import doobie.implicits._
import doobie.implicits.javatime._

import java.util.UUID

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
    implicit val category: Read[categories.output.Category] = Read[(String, String, Int)]
      .map{
      case (name, id, links) => categories.output.Category(name = name, id = UUID.fromString(id), links = links)
    }
    implicit val categoryLink: Read[CategoryLink] = Read[
      (
        String
        , String
        , String
        , String
      )
    ].map{
      case (name, id, url, linkName) => CategoryLink(
        UUID.fromString(id)
        , name
        , url
        , linkName
      )
    }
  }
}
