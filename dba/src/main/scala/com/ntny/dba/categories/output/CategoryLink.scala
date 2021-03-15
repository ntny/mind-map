package com.ntny.dba.categories.output

import java.util.UUID

case class CategoryLink(
                         categoryId: UUID
                         , categoryName: String
                         , linkUrl: String
                         , linkName: String
                       )
