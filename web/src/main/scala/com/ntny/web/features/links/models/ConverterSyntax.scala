package com.ntny.web.features.links.models

import java.time.{LocalDateTime, ZoneOffset}
import java.util.UUID

import com.ntny.dba.links.rows.NewLink

object ConverterSyntax {
  implicit class ValidatedLinkOps(link: ValidatedLink) {
    def toDbo = NewLink(
      UUID.fromString(link.ownerId.value)
      , link.url.value
      , link.name.value
      , link.description.map(_.value)
      , LocalDateTime.ofInstant(link.deadline, ZoneOffset.UTC)
    )
  }
}
