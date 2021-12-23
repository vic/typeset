package testing

import typeset.{:+:, CanBeObtained, Remaining, TNil}
import utest._

object OrderingTest extends TestSuite {
  val tests = Tests {

    test("a set with two types that can be swapped") {
      val x                           = true :+: 1 :+: TNil
      val y: Int :+: Boolean :+: TNil = x
      val v0                          = y.get[Int]
      assert(v0 == 1)
      val v1 = y.get[Boolean]
      assert(v1 == true)
    }

    test("can detect last elements swapped") {
      val x                                      = "yes" :+: true :+: 1 :+: TNil
      val y: String :+: Int :+: Boolean :+: TNil = x
      val n                                      = y.get[Int]
      assert(n == 1)
    }

    test("can detect first elements swapped") {
      val x                                      = "yes" :+: true :+: 1 :+: TNil
      val y: Int :+: Boolean :+: String :+: TNil = x
      val n                                      = y.get[Int]
      assert(n == 1)
    }

    test("can detect tail remains same") {
      val x                                      = "yes" :+: true :+: 1 :+: TNil
      val y: Boolean :+: String :+: Int :+: TNil = x
      val n                                      = y.get[Int]
      assert(n == 1)
    }

    test("can detect second elements swapped") {
      val x                                               = 'c' :+: "yes" :+: true :+: 1 :+: TNil
      val y: String :+: Char :+: Int :+: Boolean :+: TNil = x
      val n                                               = y.get[Int]
      assert(n == 1)
    }

    test("cannot reorder if left hand side has different types") {
      val a = 1 :+: true :+: TNil
      compileError("val x: Int :+: String :+: TNil = a")
        .check(
          """
            |      compileError("val x: Int :+: String :+: TNil = a")
            |                                                     ^
            |""".stripMargin
        )
    }
  }
}
