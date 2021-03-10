package com.ntny.web.features.links.models

import java.time.{LocalDateTime, ZoneOffset}
import java.util.UUID

import com.ntny.dba.links.queries.inputs.Owner
import com.ntny.dba.links.rows.NewLink

object ConverterSyntax {

  implicit class ValidatedOwnerOps(owner: ValidatedOwner) {
    def toDomain = Owner(UUID.fromString(owner.id.value))
  }

  implicit class ValidatedLinkOps(link: ValidatedLink) {
    def toDomain = NewLink(
      UUID.fromString(link.ownerId.value)
      , link.url.value
      , link.name.value
      , link.description.map(_.value)
      , LocalDateTime.ofInstant(link.deadline, ZoneOffset.UTC)
    )
  }
}
