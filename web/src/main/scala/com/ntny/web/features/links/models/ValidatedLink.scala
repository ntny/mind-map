package com.ntny.web.features.links.models

import java.time.Instant
import java.util.UUID

import eu.timepit.refined.types.string.NonEmptyString

case class ValidatedLink(
                 ownerId: UUID
                 , url: NonEmptyString
                 , name: NonEmptyString
                 , description: Option[NonEmptyString]
                 , deadline: Instant
               )
