import munit.FunSuite

class MySuite extends FunSuite {

  test("eval-value") {
    val obtained = Interpreter.eval(Happy)
    val expected = Happy
    assertEquals(obtained, expected)
  }

  test("eval-many-exprs") {
    val obtained = Interpreter.eval(ManyExprs(List(Happy, VeryHappy)))
    val expected = ManyVals(List(Happy, VeryHappy))
    assertEquals(obtained, expected)
  }

  test("eval-many-exprs-error") {
    val obtained = Interpreter.eval(ManyExprs(List(Happy, Interpreter.ERROR)))
    val expected = Interpreter.ERROR
    assertEquals(obtained, expected)
  }

  test("eval-plus-many-values") {
    val obtained = Interpreter.eval(Plus(ManyVals(List(Happy, VeryHappy)), Happy))
    val expected = ManyVals(List(Happy, VeryHappy))
    assertEquals(obtained, expected)
  }

  test("eval-plus-many-values-error") {
    val obtained = Interpreter.eval(Plus(ManyVals(List(Happy, Interpreter.ERROR)), Happy))
    val expected = Interpreter.ERROR
    assertEquals(obtained, expected)
  }

  test("eval-stay-uwu") {
    val obtained = Interpreter.eval(Plus(VeryHappy, Happy))
    val expected = VeryHappy
    assertEquals(obtained, expected)
  }

  test("eval-become-uwu") {
    val obtained = Interpreter.eval(Plus(Happy, VeryHappy))
    val expected = VeryHappy
    assertEquals(obtained, expected)
  }

  test("eval-move-on") {
    val obtained = Interpreter.eval(Plus(Cry, Happy))
    val expected = Happy
    assertEquals(obtained, expected)
  }

  test("eval-hard-day") {
    val obtained = Interpreter.eval(Plus(Happy, Cry))
    val expected = Cry
    assertEquals(obtained, expected)
  }

  test("eval-meh") {
    val obtained = Interpreter.eval(Plus(Happy, Stun))
    val expected = Happy
    assertEquals(obtained, expected)
  }

  test("eval-not-stun") {
    val obtained = Interpreter.eval(Not(Stun))
    val expected = Sleepy
    assertEquals(obtained, expected)
  }

  test("eval-not-sleepy") {
    val obtained = Interpreter.eval(Not(Sleepy))
    val expected = Stun
    assertEquals(obtained, expected)
  }

  test("eval-not-(very)-happy") {
    val obtained = Interpreter.eval(Not(Happy))
    val expected = Cry
    assertEquals(obtained, expected)
  }

  test("eval-not-cry") {
    val obtained = Interpreter.eval(Not(Cry))
    val expected = VeryHappy
    assertEquals(obtained, expected)
  }

  test("eval-not-many-values") {
    val obtained = Interpreter.eval(Not(ManyVals(List(Happy, VeryHappy))))
    val expected = Cry
    assertEquals(obtained, expected)
  }

  test("integration-test-1") {
    val obtained = Interpreter.eval(Plus(ManyVals(List(Happy, VeryHappy)), Not(ManyVals(List(Cry, Stun)))))
    val expected = ManyVals(List(Happy, VeryHappy))
    assertEquals(obtained, expected)
  }

  test("integration-test-2") {
    val obtained = Interpreter.eval(Plus(Happy, Not(ManyVals(List(Happy, Cry)))))
    val expected = Cry
    assertEquals(obtained, expected)
  }

  test("integration-test-3") {
    val obtained = Interpreter.eval(Not(Plus(ManyVals(List(Happy, Cry)), VeryHappy)))
    val expected = VeryHappy
    assertEquals(obtained, expected)
  }

  test("integration-test-4") {
    val obtained = Interpreter.eval(Not(Plus(Cry, VeryHappy)))
    val expected = VeryHappy
    assertEquals(obtained, expected)
  }

  test("integration-test-5") {
    val obtained = Interpreter.eval(Plus(Not(Happy), VeryHappy))
    val expected = VeryHappy
    assertEquals(obtained, expected)
  }

  test("empty-many-exprs") {
    val obtained = Interpreter.eval(ManyExprs(Nil))
    val expected = ManyVals(Nil)
    assertEquals(obtained, expected)
  }

  test("nested-expressions") {
    val expr = Plus(Not(Plus(Happy, Cry)), ManyVals(List(VeryHappy, Stun)))
    val obtained = Interpreter.eval(expr)
    val expected = ManyVals(List(Cry, Stun))
    assertEquals(obtained, expected)
  }

  test("combining-multiple-operations") {
    val expr = Not(Plus(Count(ManyVals(List(Happy, Cry, Happy)), Not(Sleepy)), VeryHappy))
    val obtained = Interpreter.eval(expr)
    val expected = Cry
    assertEquals(obtained, expected)
  }
}
