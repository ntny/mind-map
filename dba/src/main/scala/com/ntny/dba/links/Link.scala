package com.ntny.dba.links

import java.time.LocalDateTime

case class Link(
            url: String
            , name: String
            , description: Option[String]
            , deadline: LocalDateTime)
