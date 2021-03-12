package com.ntny.dba.links.queries.output

import java.time.LocalDateTime

case class Link(
                 url: String
                 , name: String
                 , description: Option[String]
                 , deadline: LocalDateTime)
