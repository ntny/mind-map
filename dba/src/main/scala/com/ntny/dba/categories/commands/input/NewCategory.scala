package com.ntny.dba.categories.commands.input

import com.ntny.dba.{CategoryName, Owner}

case class NewCategory(ownerId: Owner, name: CategoryName)
