package testing

import typeset.TNil
import utest._

object ObtainingTest extends TestSuite {
  val tests = Tests {

    test("obtaining head of singleton") {
      val x = 1 :+: TNil
      val n = x.get[Int]
      assert(n == 1)
    }

    test("obtaining inside of set") {
      val x = "sure" :+: 1 :+: false :+: TNil
      val n = x.get[Int]
      assert(n == 1)
    }

    test("cannot obtain from set not containing type") {
      val a = 1 :+: true :+: TNil
      compileError("val x = a.get[String]")
        .check(
          """
            |      compileError("val x = a.get[String]")
            |                                 ^
            |""".stripMargin,
          "Cannot obtain type String from Int :+: Boolean :+: typeset.TNil"
        )
    }
  }
}
