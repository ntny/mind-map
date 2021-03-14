package com.ntny.web.middleware.authentication.output

import eu.timepit.refined.api.Refined
import eu.timepit.refined.string.Uuid

case class AuthenticatedUser(id: String Refined Uuid)
