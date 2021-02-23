package com.ntny.models

import java.time.Instant

import eu.timepit.refined.types.string.NonEmptyString

case class Link(
                 url: NonEmptyString
                 , name: NonEmptyString
                 , description: Option[NonEmptyString]
                 , deadline: Instant
               )
