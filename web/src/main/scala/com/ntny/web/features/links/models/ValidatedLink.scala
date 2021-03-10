package com.ntny.web.features.links.models

import eu.timepit.refined.api.Refined
import eu.timepit.refined.boolean.And
import eu.timepit.refined.collection.{MaxSize, MinSize, NonEmpty, Size}
import eu.timepit.refined.string.{Url, Uuid}

import java.time.Instant
import eu.timepit.refined.types.string.NonEmptyString


case class ValidatedLink(
                 ownerId: String Refined Uuid
                 , url: String Refined Url
                 , name: String Refined And[NonEmpty, MaxSize[300]]
                 , description: Option[NonEmptyString]
                 , deadline: Instant
               )

object ValidatedLink{
  import eu.timepit.refined.auto._
  var c: String Refined And[NonEmpty, MaxSize[3]] = "d"
}
