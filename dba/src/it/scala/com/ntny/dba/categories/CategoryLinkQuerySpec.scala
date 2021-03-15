package com.ntny.dba.categories

import java.time.LocalDateTime
import java.util.UUID

import com.dimafeng.testcontainers.PostgreSQLContainer
import com.ntny.dba.categories.commands.PutCategory
import com.ntny.dba.categories.output.CategoryLink
import com.ntny.dba.categories.queries.CategoryLinkQuery
import com.ntny.dba.links.commands.PutLinkCommand
import com.ntny.dba.links.commands.input.NewLink
import com.ntny.dba.{AuthenticatedOwner, NewCategoryName, PostgresSqlTest}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers

class CategoryLinkQuerySpec extends AnyFlatSpec with Matchers with PostgresSqlTest  {
  override val container: PostgreSQLContainer = PostgreSQLContainer()
  import doobie.implicits._
  import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

  it should "correct get CategoryLinks from db" in {
    val ownerId1 = UUID.randomUUID()

    val categoryId1 = PutCategory(AuthenticatedOwner(ownerId1), NewCategoryName("category-1")).transact(xa).unsafeRunSync()
    val categoryId2 = PutCategory(AuthenticatedOwner(ownerId1), NewCategoryName("category-2")).transact(xa).unsafeRunSync()
    PutLinkCommand(AuthenticatedOwner(ownerId1), NewLink(
      categoryId1
      ,"category-1-link.com"
      ,"category-1-link"
      , None
      , LocalDateTime.now()
    )).transact(xa).unsafeRunSync()
    PutLinkCommand(AuthenticatedOwner(ownerId1), NewLink(
      categoryId1
      ,"category-2-link.com"
      ,"category-2-link"
      , None
      , LocalDateTime.now()
    )).transact(xa).unsafeRunSync()
    PutLinkCommand(AuthenticatedOwner(ownerId1), NewLink(
      categoryId2
      ,"category-3-link.com"
      ,"category-3-link"
      , None
      , LocalDateTime.now()
    )).transact(xa).unsafeRunSync()

    val actual = CategoryLinkQuery(AuthenticatedOwner(ownerId1)).transact(xa).unsafeRunSync()
    val expected = List(
      CategoryLink(categoryId1, "category-1", "category-1-link.com", "category-1-link")
      , CategoryLink(categoryId1, "category-1", "category-2-link.com", "category-2-link")
      , CategoryLink(categoryId2, "category-2", "category-3-link.com", "category-3-link")
    )
    actual should contain theSameElementsAs expected
  }
}
