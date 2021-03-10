package com.ntny.web.features.links.models

import eu.timepit.refined.api.Refined
import eu.timepit.refined.string.Uuid

case class ValidatedOwner(id: String Refined Uuid)
