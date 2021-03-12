package com.ntny.web.features.links.input

import eu.timepit.refined.api.Refined
import eu.timepit.refined.boolean.And
import eu.timepit.refined.collection.{MaxSize, NonEmpty}
import eu.timepit.refined.string.{Url, Uuid}

import java.time.Instant
import eu.timepit.refined.types.string.NonEmptyString


case class ValidatedNewLink(
                             ownerId: String Refined Uuid
                             , categoryId: String Refined Uuid
                             , url: String Refined Url
                             , name: String Refined And[NonEmpty, MaxSize[300]]
                             , description: Option[NonEmptyString]
                             , deadline: Instant
                           )
