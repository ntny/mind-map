package com.ntny.dba

import java.util.UUID

case class Owner(id: UUID) extends AnyVal
case class CategoryId(id: UUID) extends AnyVal
case class CategoryName(name: String) extends AnyVal

case class CategoryLinkParams(owner: Owner, category: CategoryId)

