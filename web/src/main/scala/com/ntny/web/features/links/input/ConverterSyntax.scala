package com.ntny.web.features.links.input

import com.ntny.dba.{AuthenticatedOwner, CategoryId, NewCategoryName}

import java.time.{LocalDateTime, ZoneOffset}
import java.util.UUID
import com.ntny.dba.links.commands.input.NewLink
import com.ntny.web.features.authentification.input.AuthenticatedUser
import com.ntny.web.features.categories.input.ValidatedCategoryName
import com.ntny.web.features.cross.input.ValidatedCategoryId

object ConverterSyntax {

  implicit class ValidatedCategoryIdOps(c: ValidatedCategoryId) {
    def toDomain = CategoryId(UUID.fromString(c.id.value))
  }

  implicit class ValidatedCategoryNameOps(c: ValidatedCategoryName) {
    def toDomain = NewCategoryName(c.name.value)
  }

  implicit class AuthenticatedUserOps(owner: AuthenticatedUser) {
    def toDomain = AuthenticatedOwner(UUID.fromString(owner.id.value))
  }

  implicit class ValidatedLinkOps(link: ValidatedNewLink) {
    def toDomain = NewLink(
      UUID.fromString(link.categoryId.value)
      , link.url.value
      , link.name.value
      , link.description.map(_.value)
      , LocalDateTime.ofInstant(link.deadline, ZoneOffset.UTC)
    )
  }
}
