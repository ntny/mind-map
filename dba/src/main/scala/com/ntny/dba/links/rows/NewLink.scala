package com.ntny.dba.links.rows

import java.time.LocalDateTime
import java.util.UUID


case class NewLink(
                    ownerId: UUID
                   , url: String
                   , name: String
                   , description: Option[String]
                   , deadline: LocalDateTime)
