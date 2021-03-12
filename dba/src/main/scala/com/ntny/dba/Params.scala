package com.ntny.dba

import java.util.UUID

case class Owner(id: UUID) extends AnyVal
case class Category(id: UUID) extends AnyVal

case class CategoryLinkParams(owner: Owner, category: Category)
