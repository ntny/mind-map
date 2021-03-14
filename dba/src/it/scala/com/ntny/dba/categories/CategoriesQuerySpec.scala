package com.ntny.dba.categories

import com.dimafeng.testcontainers.PostgreSQLContainer
import com.ntny.dba.{AuthenticatedOwner, NewCategoryName, PostgresSqlTest}
import com.ntny.dba.categories.commands.PutCategory
import com.ntny.dba.categories.output.Category
import com.ntny.dba.categories.queries.CategoriesQuery
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

import java.util.UUID

class CategoriesQuerySpec extends AnyFlatSpec with Matchers with PostgresSqlTest  {
  override val container: PostgreSQLContainer = PostgreSQLContainer()
  import doobie.implicits._

  it should "correct get owner categories from db" in {
    val ownerId1 = UUID.randomUUID()
    val ownerId2 = UUID.randomUUID()

    val categoryId1 = PutCategory(AuthenticatedOwner(ownerId1), NewCategoryName("name-1")).transact(xa).unsafeRunSync()
    val categoryId2 = PutCategory(AuthenticatedOwner(ownerId1), NewCategoryName("name-2")).transact(xa).unsafeRunSync()
    PutCategory(AuthenticatedOwner(ownerId2), NewCategoryName("name-3")).transact(xa).unsafeRunSync()

    val actual = CategoriesQuery(AuthenticatedOwner(ownerId1)).transact(xa).unsafeRunSync()
    val expected = List(
      Category(
        name = "name-1"
        , id = categoryId1
      ),
      Category(
        name = "name-2",
        id = categoryId2
      )
    )
    actual should contain theSameElementsAs expected
  }
}
