package com.ntny.web.features.authentification.input

import eu.timepit.refined.api.Refined
import eu.timepit.refined.string.Uuid

case class AuthenticatedUser(id: String Refined Uuid)
