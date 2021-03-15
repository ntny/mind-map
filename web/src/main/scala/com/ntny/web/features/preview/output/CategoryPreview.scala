package com.ntny.web.features.preview.output

import java.util.UUID

case class CategoryPreview(categoryId: UUID, categoryName: String, links: List[LinkPreview])

case class LinkPreview(name: String, url: String)

