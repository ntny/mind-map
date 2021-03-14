package com.ntny.web.features.categories.input

import eu.timepit.refined.types.string.NonEmptyString

case class ValidatedCategoryName(name: NonEmptyString)
