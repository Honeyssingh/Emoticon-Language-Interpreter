class MySuite extends munit.FunSuite {
  //Basic implementation tests
  test("Happy test"){
    val expr = Happy

    val obtained = expr.eval

    assertEquals(obtained, Happy)
  }

  test("Cry test"){
    val expr = Cry

    val obtained = expr.eval

    assertEquals(obtained, Cry)
  }

  test("Very Happy test"){
    val expr = VeryHappy

    val obtained = expr.eval

    assertEquals(obtained, VeryHappy)
  }

  test("Sleepy test"){
    val expr = Sleepy

    val obtained = expr.eval

    assertEquals(obtained, Sleepy)
  }

  test("Stun test"){
    val expr = Stun

    val obtained = expr.eval

    assertEquals(obtained, Stun)
  }

  //Many Expr tests
  test("ManyExpr to ManyVals test"){
    val expr = ManyExprs(List(Stun, Cry))
    val obtained = expr.eval
    assert(obtained == ManyVals(List(Stun, Cry)))
  }

  test("ManyExpr to ManyVals test 2"){
    val expr = ManyExprs(List(Happy, Cry, VeryHappy))
    val obtained = expr.eval
    assert(obtained == ManyVals(List(Happy, Cry, VeryHappy)))
  }

  //Plus tests
  test("Plus Happy Happy test") {
    val expr = Plus(Happy, Happy)

    val obtained = expr.eval
    // print(obtained)
    assertEquals(obtained, Happy)
  }
  
  test("Plus Happy stun test") {
    val expr = Plus(Happy, Stun)

    val obtained = expr.eval
    // print(obtained)
    assertEquals(obtained, Happy)
  }

  test("Plus Cry VeryHappy test") {
    val expr = Plus(Cry, VeryHappy)

    val obtained = expr.eval
    // print(obtained)
    assertEquals(obtained, VeryHappy)
  }
  test("Plus Happy Cry test") {
    val expr = Plus(Happy, Cry)

    val obtained = expr.eval
    // print(obtained)
    assertEquals(obtained, Cry)
  }

  test("Plus Stun Cry test") {
    val expr = Plus(Stun, Cry)

    val obtained = expr.eval
    // print(obtained)
    assertEquals(obtained, Cry)
  }

  test("Plus meh test") {
    val expr = Plus(Stun, Happy)

    val obtained = expr.eval
    // print(obtained)
    assertEquals(obtained, Stun)
  }

  test("Plus Manyvals test") {
    val expr = Plus(ManyVals(List(Happy, Stun)), Cry)

    val obtained = expr.eval
    // print(obtained)
    assertEquals(obtained, ManyVals(List(Cry, Cry)))
  }

  test("Plus Manyvals test second parameter") {
    val expr = Plus(Happy, ManyVals(List(VeryHappy, Cry)))

    val obtained = expr.eval
    // print(obtained)
    assertEquals(obtained, ManyVals(List(VeryHappy, Cry)))
  }

  test("Plus Manyvals test second parameter number 2") {
    val expr = Plus(Stun, ManyVals(List(VeryHappy, Cry)))

    val obtained = expr.eval
    // print(obtained)
    assertEquals(obtained, ManyVals(List(VeryHappy, Cry)))
  }

  test("Plus Manyvals test second parameter number 3") {
    val expr = Plus(Cry, ManyVals(List(VeryHappy, Stun)))

    val obtained = expr.eval
    // print(obtained)
    assertEquals(obtained, ManyVals(List(VeryHappy, Stun)))
  }

  test("Plus 2 Manyvals test") {
    val expr = Plus(ManyVals(List(Cry, Happy)), ManyVals(List(VeryHappy, Cry)))

    val exception = intercept[RuntimeException] {
            expr.eval
        }
    assert(exception.getMessage == "Cannot add multivals to multivals")
    
  }

  test("Plus 2 Manyvals test 2") {
    val expr = Plus(ManyVals(List(VeryHappy, Happy)), ManyVals(List(Cry, Sleepy)))

    val exception = intercept[RuntimeException] {
            expr.eval
        }
    assert(exception.getMessage == "Cannot add multivals to multivals")
  }


  //Not Tests
  test("Not VeryHappy test") {
    val expr = Not(VeryHappy)

    val obtained = expr.eval
    print(obtained)
    assertEquals(obtained, Cry)
  }
  test("Not Sleepy test") {
    val expr = Not(Sleepy)

    val obtained = expr.eval
    print(obtained)
    assertEquals(obtained, Stun)
  }

  test("Not Multival test") {
    val expr = Not(ManyVals(List(Cry, Stun, Sleepy, Happy)))

    val obtained = expr.eval
    print(obtained)
    assertEquals(obtained, Stun)
  }
  //Count tests
  test("Count test 1"){
    val expr = Count(Cry, Happy)

    val exception = intercept[RuntimeException] {
            expr.eval
        }
    assert(exception.getMessage == "There is no implementation instructions for count T.T")
  }

  test("Count test 2"){
   val expr = Count(ManyExprs(List(Cry, Sleepy)), VeryHappy)
    
    val exception = intercept[RuntimeException] {
            expr.eval
        }
    assert(exception.getMessage == "There is no implementation instructions for count T.T")
  }

  //
  //Plus of manyvals and not manyvals >:)
  test("Complex test 1") {
    val expr = Plus(ManyVals(List(Cry, VeryHappy, Stun, Sleepy, Happy)),Not(ManyVals(List(Cry, Stun, Sleepy, Happy))))

    val obtained = expr.eval
    //print(obtained)
    assertEquals(obtained, ManyVals(List(Stun, VeryHappy, Stun, Sleepy, Happy)))
  }

  //failure due to no count implementation
  //Basically, we hid a count deep in this example, and want to fail because of it
  test("Complex test 2") {
    val ManyVals = ManyExprs(List(Cry, VeryHappy, Stun, Sleepy, Happy, Count(Not(Happy), Plus(Happy, Happy))))

    val exception = intercept[RuntimeException] {
            ManyVals.eval
        }
    assert(exception.getMessage == "There is no implementation instructions for count T.T")
  }
  // Testing the plus of two nots
  test("Complex test 3") {
    val expr = Plus(Not(ManyVals(List(Cry, VeryHappy))), Not(ManyVals(List(Sleepy, Stun))))
    val expected = VeryHappy
    assert(expr.eval == expected)
  }
  test("Complex test 4"){
    val expr = Not(Plus(ManyExprs(List(Not(VeryHappy), Not(Happy), Plus(Not(Happy), Happy))), Not(Cry)))
    val obtained = expr.eval

    assertEquals(obtained, VeryHappy)
  }
  //What happens if you not a plus? Also throw in manyvals and many expressions and throw like a billion nested things
  test("Complex test 5"){
    val expr = Not(Plus(Not(ManyExprs(List(Not(Sleepy), Not(Stun), Plus(Cry, Stun)))), ManyVals(List(Stun, Sleepy))))
    val obtained = expr.eval

    assertEquals(obtained, Stun)
  }
   //not many exprs, and nested a bunch
  test("Complex test 6") {
    val expr = Plus(
      Not(ManyExprs(List(Plus(Happy, Cry), Not(Plus(Stun, Sleepy)), Plus(Cry, VeryHappy)))), Plus(Not(Happy), ManyExprs(List(Not(Plus(Cry, Sleepy)), Plus(Stun, VeryHappy)))))
    val expected = ManyVals(List(
      VeryHappy,   
      VeryHappy   
    ))

    val obtained = expr.eval

    assertEquals(obtained, expected)
  } 
  // Deep nesting with alternating Not and Plus
  test("Complex test 7") {
    val expr = Not(Plus(
      Not(Plus(
        Not(Happy),
        Plus(
          ManyVals(List(Cry, Stun)),
          Not(Sleepy)
        )
      )),
      Not(Plus(Cry, VeryHappy))
    ))

    val obtained = expr.eval
    println(s"Obtained: $obtained")
    assertEquals(obtained, VeryHappy)
  }
}


