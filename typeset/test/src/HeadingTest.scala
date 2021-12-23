package testing

import typeset.{:+:, TNil}
import utest._

object HeadingTest extends TestSuite {
  val tests = Tests {

    test("anything can be head for TNil") {
      val _: Int :+: TNil = 1 :+: TNil
    }

    test("a set cannot be head of other set") {
      compileError("val x = TNil :+: TNil")
        .check(
          """
            |      compileError("val x = TNil :+: TNil")
            |                                 ^
            |""".stripMargin,
          "Cannot use a TSet as head of another TSet"
        )
    }

    test("cannot use same type head twice") {
      compileError("val x = 1 :+: 2 :+: TNil")
        .check(
          """
            |      compileError("val x = 1 :+: 2 :+: TNil")
            |                              ^
            |""".stripMargin,
          "Cannot use Int as head for Int :+: typeset.TNil"
        )
    }
    test("cannot use same type head twice") {
      val a = 1 :+: TNil
      compileError("val x = 2 :+: a")
        .check(
          """
            |      compileError("val x = 2 :+: a")
            |                              ^
            |""".stripMargin,
          "Cannot use Int as head for Int :+: typeset.TNil"
        )
    }

    test("cannot use same type as head when its part of tail") {
      val a = true :+: 1 :+: TNil
      compileError("val x = 2 :+: a")
        .check(
          """
            |      compileError("val x = 2 :+: a")
            |                              ^
            |""".stripMargin,
          "Type Int already exists in Boolean :+: Int :+: typeset.TNil"
        )
    }

    test("can add consecutive heads") {
      val _: Boolean :+: String :+: TNil = true :+: "foo" :+: TNil
    }
  }
}
