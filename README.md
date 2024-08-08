<<<<<<< HEAD
### README.md
=======
>>>>>>> refs/remotes/origin/main

# Emoticon Language Interpreter

## Overview

This project implements an interpreter for a simple language that uses emoticons as values and supports basic operations on these emoticons. The language supports expressions, lists of expressions, and a set of operations (`Plus`, `Not`, and `Count`). The interpreter is implemented in Scala and includes a comprehensive suite of tests.

## Language Specification

### Concrete Syntax

```plaintext
e ::= v
    | (e)
    | [ e* ]       // a list of 0-or-many expressions
    | e1 + e2
    | ! e1
    | e1 count e2

v ::= T.T
    | :)
    | UwU
    | -.- zzZ
    | \circ . \circ
    | [ v* ]       // a list of 0-or-many values
```

### Abstract Syntax

```plaintext
Expr ::= Value
       | ManyExprs(Expr*)
       | Plus(Expr1, Expr2)
       | Not(Expr1)
       | Count(Expr1, Expr2)

Value ::= Cry        // T.T
        | Happy      // :)
        | VeryHappy  // UwU
        | Sleepy     // -.- zzZ
        | Stun       // \circ . \circ
        | ManyVals(Value*)
```

### Evaluation Rules

The evaluation rules define how expressions are evaluated to produce values. Some key rules include:

- `eval(v) = v`
- `eval(ManyExprs(e1, ..., en)) = ManyVals(v1, ..., vn)` if all `ei` evaluate to `vi`
- `eval(Plus(e1, e2))` follows specific rules based on the values of `e1` and `e2`
- `eval(Not(e1))` toggles certain values
- `eval(Count(e1, e2))` is not implemented and will raise an exception

## Code Explanation

### Abstract Syntax Implementation

The abstract syntax is implemented using Scala case classes and traits. Here's a brief overview:

- `Expr` is the base trait for all expressions.
- `Value` is a trait extending `Expr` and represents emoticon values.
- `ManyExprs` represents a list of expressions.
- `Plus`, `Not`, and `Count` represent the operations.

### Evaluation Method

Each `Expr` has an `eval` method that evaluates the expression according to the specified rules:

- **Value**: Returns itself.
- **ManyExprs**: Evaluates each expression in the list and returns a `ManyVals` containing the results.
- **Plus**: Evaluates `e1` and `e2`, then applies specific rules based on their values.
- **Not**: Toggles the value of `e1` based on specific rules.
- **Count**: Not implemented and raises an exception.

### Example Code

Here's a snippet of the abstract syntax implementation:

```scala
sealed trait Expr {
  def eval: Value = {
    this match {
      case v: Value => v
      case ManyExprs(lst) => 
        val evaledList = lst.map(_.eval)
        if (evaledList.contains(ErrorValue)) ErrorValue else ManyVals(evaledList)
      case Plus(e1, e2) => 
        val v1 = e1.eval
        val v2 = e2.eval
        (v1, v2) match {
          case (ManyVals(lst), ManyVals(lst2)) => throw new RuntimeException("Cannot add ManyVals to ManyVals")
          case (ManyVals(lst), _) => 
            val evaledList = lst.map(x => Plus(x, v2).eval)
            if (evaledList.contains(ErrorValue)) ErrorValue else ManyVals(evaledList)
          case (_, ManyVals(lst)) => 
            val evaledList = lst.map(x => Plus(v1, x).eval)
            if (evaledList.contains(ErrorValue)) ErrorValue else ManyVals(evaledList)
          case (VeryHappy, _) => VeryHappy
          case (_, VeryHappy) => VeryHappy
          case (Cry, _) => v2
          case (Happy, Cry) => Cry
          case (Stun, Cry) => Cry
          case (v1, v2) if v1 != Cry && v1 != VeryHappy && v2 != Cry && v2 != VeryHappy => v1
          case _ => ErrorValue
        }
      case Not(e) => 
        val v = e.eval
        v match {
          case Stun => Sleepy
          case Sleepy => Stun
          case Happy => Cry
          case VeryHappy => Cry
          case Cry => VeryHappy
          case ManyVals(lst) => 
            if (lst.length < 2) ErrorValue
            else lst.tail.foldLeft(lst.head) {(acc, elem) => Plus(acc, elem).eval}
          case _ => ErrorValue
        }
      case Count(e1, e2) => throw new RuntimeException("There is no implementation instructions for count T.T")
      case _ => ErrorValue
    }
  }
}

sealed trait Value extends Expr

case class ManyExprs(lst: List[Expr]) extends Expr
case class Plus(e1: Expr, e2: Expr) extends Expr
case class Not(e: Expr) extends Expr
case class Count(e1: Expr, e2: Expr) extends Expr

case object Cry extends Value
case object Happy extends Value
case object VeryHappy extends Value
case object Sleepy extends Value
case object Stun extends Value
case object ErrorValue extends Value
case class ManyVals(v: List[Value]) extends Value
```

## Testing

### Unit Tests

We have unit tests for each operational semantic and additional tests for complex expressions. Here's an overview:

- **Value Tests**: Verify that values evaluate to themselves.
- **ManyExprs Tests**: Verify that lists of expressions evaluate correctly.
- **Plus Tests**: Verify the various rules for the `Plus` operation.
- **Not Tests**: Verify the various rules for the `Not` operation.
- **Count Tests**: Verify that `Count` raises the expected exception.

### Integration Tests

We also have integration tests for more complex expressions that combine multiple operations and values.

### Example Test

Here's an example unit test:

```scala
test("Plus Happy Cry test") {
  val expr = Plus(Happy, Cry)
  val obtained = expr.eval
  assertEquals(obtained, Cry)
}
```

<<<<<<< HEAD
And an example integration test:

```scala
test("Complex test 5") {
  val expr = Not(Plus(Not(ManyExprs(List(Not(Sleepy), Not(Stun), Plus(Cry, Stun)))), ManyVals(List(Stun, Sleepy))))
  val obtained = expr.eval
  assertEquals(obtained, Stun)
}
```

## Potential Evaluation Cases Missed

The current implementation does not handle the following cases:

- The `Count` operation is not implemented and raises an exception.
- The behavior for deeply nested and mixed operations may need further specification and testing.

## Running the Tests

To run the tests, use the following command:

```sh
sbt test
```

This will execute all unit and integration tests and report any failures.

## Conclusion

This project demonstrates a simple language interpreter for emoticons with basic operations and comprehensive testing. The provided implementation and tests ensure that the language behaves as expected according to the specified semantics.
=======
1. Please SAVE off the original test(s) and interpreter(s) in some way so we can see the evolution of your work.
2. You must translate the abstract syntax provided above to Scala code. 
3. Run this result against your created tests.
4. Identify any potential cases of evaluation that the language designers failed to specify (recommend doing this while working on the above tasks). **highlight whatever you find this time around that you did not find the first time through**
a
## STRETCH: Language Extension
Only if you have successfully completed the above task (with course staff sign off), should you move on to the following task.

* Extend the language with any of the following and provide a complete operational semantic
    * additional values
    * additional expressions
    * additional collections
    * features such as variables, functions, mutabiles and loops
    
* Once you have completed the operational semantics for your change to the language, you may extend the interpreter. Be sure to test this new work. Please SAVE off the original interpreter(s) in some way so we can see the evolution of the interpreter.
>>>>>>> refs/remotes/origin/main
