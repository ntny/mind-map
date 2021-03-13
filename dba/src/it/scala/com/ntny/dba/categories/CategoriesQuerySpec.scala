package com.ntny.dba.categories

import com.dimafeng.testcontainers.{ForAllTestContainer, PostgreSQLContainer}
import com.ntny.dba.{CategoryName, Owner, PostgresSqlTest}
import com.ntny.dba.categories.commands.PutCategory
import com.ntny.dba.categories.commands.input.NewCategory
import com.ntny.dba.categories.output.Category
import com.ntny.dba.categories.queries.CategoriesQuery
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

import java.util.UUID

class CategoriesQuerySpec extends AnyFlatSpec with Matchers with ForAllTestContainer with PostgresSqlTest  {
  override val container: PostgreSQLContainer = PostgreSQLContainer()
  import doobie.implicits._

  it should "correct get owner categories from db" in {
    val ownerId1 = UUID.randomUUID()
    val ownerId2 = UUID.randomUUID()

    PutCategory(NewCategory(Owner(ownerId1), CategoryName("name-1"))).transact(xa).unsafeRunSync()
    PutCategory(NewCategory(Owner(ownerId1), CategoryName("name-2"))).transact(xa).unsafeRunSync()
    PutCategory(NewCategory(Owner(ownerId2), CategoryName("name-3"))).transact(xa).unsafeRunSync()

    val actual = CategoriesQuery(Owner(ownerId1)).transact(xa).unsafeRunSync()
    val expected = List(
      Category(
        "name-1"
      ),
      Category(
        "name-2"
      )
    )
    actual should contain theSameElementsAs expected
  }
}
