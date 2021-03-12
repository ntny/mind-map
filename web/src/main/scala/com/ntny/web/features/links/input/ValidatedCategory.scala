package com.ntny.web.features.links.input

import eu.timepit.refined.api.Refined
import eu.timepit.refined.string.Uuid

case class ValidatedCategory(id: String Refined Uuid)
