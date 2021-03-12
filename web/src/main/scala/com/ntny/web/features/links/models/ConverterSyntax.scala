package com.ntny.web.features.links.models

import java.time.{LocalDateTime, ZoneOffset}
import java.util.UUID
import com.ntny.dba.links.queries.input.{Category, CategoryLinkParams, Owner}
import com.ntny.dba.links.commands.input.NewLink

object ConverterSyntax {

  implicit class ValidatedCategoryOps(p: (ValidatedOwner, ValidatedCategory)) {
    def toDomain = CategoryLinkParams(
      Owner(UUID.fromString(p._1.id.value)),
      Category(UUID.fromString(p._2.id.value))
    )
  }

  implicit class ValidatedOwnerOps(owner: ValidatedOwner) {
    def toDomain = Owner(UUID.fromString(owner.id.value))
  }

  implicit class ValidatedLinkOps(link: ValidatedNewLink) {
    def toDomain = NewLink(
      UUID.fromString(link.ownerId.value)
      , UUID.fromString(link.categoryId.value)
      , link.url.value
      , link.name.value
      , link.description.map(_.value)
      , LocalDateTime.ofInstant(link.deadline, ZoneOffset.UTC)
    )
  }
}
