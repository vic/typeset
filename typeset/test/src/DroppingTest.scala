package testing

import typeset.{:+:, CanBeObtained, Remaining, TNil}
import utest._

object DroppingTest extends TestSuite {
  val tests = Tests {

    test("dropping head of singleton") {
      val x                 = 1 :+: TNil
      val (n: Int, y: TNil) = x.drop[Int]()
      assert(n == 1)
    }

    test("dropping inside of set") {
      val x                                          = "sure" :+: 1 :+: false :+: TNil
      val (n: Int, y: (String :+: Boolean :+: TNil)) = x.drop[Int]()
      assert(n == 1)
    }

    test("can obtain the remaining type") {
      val x                                    = "hey" :+: 1 :+: TNil
      val leftOver: Remaining[String :+: TNil] = x.remaining[Int, String :+: TNil]
    }

    test("cannot drop from set not containing type") {
      val a = 1 :+: true :+: TNil
      compileError("val x = a.drop[String]()")
        .check(
          """
            |      compileError("val x = a.drop[String]()")
            |                                          ^
            |""".stripMargin,
          "Cannot obtain type String from Int :+: Boolean :+: typeset.TNil"
        )
    }
  }
}
