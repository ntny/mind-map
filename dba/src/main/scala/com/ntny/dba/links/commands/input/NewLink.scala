package com.ntny.dba.links.commands.input

import java.time.LocalDateTime
import java.util.UUID


case class NewLink(
                    ownerId: UUID
                   , categoryId: UUID
                   , url: String
                   , name: String
                   , description: Option[String]
                   , deadline: LocalDateTime)
