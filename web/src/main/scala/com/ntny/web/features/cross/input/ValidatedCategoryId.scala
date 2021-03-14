package com.ntny.web.features.cross.input

import eu.timepit.refined.api.Refined
import eu.timepit.refined.string.Uuid

case class ValidatedCategoryId(id: String Refined Uuid)
