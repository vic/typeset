package testing

import typeset.{:+:, CanBeObtained, Remaining, TNil}
import utest._

object JoiningTest extends TestSuite {
  val tests = Tests {

    test("joining with TNil just gives back the left set") {
      val x               = 1 :+: TNil
      val y: Int :+: TNil = x :+: TNil
      assert(x == y)
    }

    test("can join with left singleton") {
      val x                           = false :+: TNil
      val y                           = 1 :+: TNil
      val z: Boolean :+: Int :+: TNil = x :+: y
      val n                           = z.get[Int]
      assert(n == 1)
    }

    test("can join with left set") {
      val x                                      = "JA" :+: false :+: TNil
      val y                                      = 1 :+: TNil
      val z: String :+: Boolean :+: Int :+: TNil = x :+: y
      val n                                      = z.get[Int]
      assert(n == 1)
    }

    test("cannot join with duplicated types") {
      val a = 1 :+: true :+: TNil
      compileError("val x = false :+: a")
        .check(
          """
            |      compileError("val x = false :+: a")
            |                                  ^
            |""".stripMargin,
          "Type Boolean already exists in Int :+: Boolean :+: typeset.TNil"
        )
    }
  }
}
