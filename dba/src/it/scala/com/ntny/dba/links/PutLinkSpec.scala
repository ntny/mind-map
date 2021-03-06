package com.ntny.dba.links

import java.time.LocalDateTime
import java.util.UUID
import com.dimafeng.testcontainers.PostgreSQLContainer
import com.ntny.dba.{AuthenticatedOwner, CategoryId, NewCategoryName, PostgresSqlTest}
import com.ntny.dba.categories.commands.PutCategory
import com.ntny.dba.links.commands.PutLinkCommand
import com.ntny.dba.links.commands.input.NewLink
import com.ntny.dba.links.queries.CategoryLinksQuery
import com.ntny.dba.links.queries.output.Link
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

class PutLinkSpec extends AnyFlatSpec with Matchers  with PostgresSqlTest {
  override val container: PostgreSQLContainer = PostgreSQLContainer()
  import doobie.implicits._


  it should "correct put Link into db" in {
    val ownerId = UUID.randomUUID()
    val now = LocalDateTime.now()
    val categoryId = PutCategory(AuthenticatedOwner(ownerId), NewCategoryName("category")).transact(xa).unsafeRunSync()
    val link = NewLink(
       categoryId
      , "www.link.com"
      , "name-1"
      , Some("desciption")
      , now
    )
    PutLinkCommand(AuthenticatedOwner(ownerId), link).transact(xa).unsafeRunSync()

    val actual =
      sql"""
        SELECT name FROM links WHERE category_id = ${categoryId.toString}::uuid AND owner_id = ${ownerId.toString}::uuid
         """.query[String].to[List].transact(xa).unsafeRunSync()
     val expected = List("name-1")
     actual should contain theSameElementsAs expected
  }

  it should "correct select Links from db" in {
    val ownerId = UUID.randomUUID()
    val now = LocalDateTime.now()
    val categoryId = PutCategory(AuthenticatedOwner(ownerId), NewCategoryName("category")).transact(xa).unsafeRunSync()
    val link1 = NewLink(
       categoryId
      , "www.link.com-1"
      , "name-1"
      , Some("description-1")
      , now
    )
    val link2 = NewLink(
       categoryId
      , "www.link.com-2"
      , "name-2"
      , Some("description-2")
      , now
    )

    val categoryId2 = PutCategory(AuthenticatedOwner(ownerId), NewCategoryName("category-2")).transact(xa).unsafeRunSync()
    val link3 = NewLink(
       categoryId2
      , "www.link.com-3"
      , "name-3"
      , Some("description-2")
      , now
    )

    PutLinkCommand(AuthenticatedOwner(ownerId), link1).transact(xa).unsafeRunSync()
    PutLinkCommand(AuthenticatedOwner(ownerId), link2).transact(xa).unsafeRunSync()
    PutLinkCommand(AuthenticatedOwner(ownerId), link3).transact(xa).unsafeRunSync()

    val actual = CategoryLinksQuery(AuthenticatedOwner(ownerId), CategoryId(categoryId))
      .transact(xa).unsafeRunSync()

    val expected = List(
      Link(
        "www.link.com-1"
        , "name-1"
        , Some("description-1")
        , now
      )
      , Link(
        "www.link.com-2"
        , "name-2"
        , Some("description-2")
        , now
      )
    )
    actual should contain theSameElementsAs expected
  }
}
