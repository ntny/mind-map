package com.ntny.web.features.links.models

import java.time.Instant

import eu.timepit.refined.types.string.NonEmptyString

case class ValidatedLink(
                 ownerId: NonEmptyString
                 , url: NonEmptyString
                 , name: NonEmptyString
                 , description: Option[NonEmptyString]
                 , deadline: Instant
               )
