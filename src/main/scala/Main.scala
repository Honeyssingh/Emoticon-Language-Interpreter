// Sealed trait for all expressions
sealed trait Expr

// Sealed trait for values, which are a subset of expressions
sealed trait Value extends Expr

// Case classes representing different emoticon values
case class Cry() extends Value
case class Happy() extends Value
case class VeryHappy() extends Value
case class Sleepy() extends Value
case class Stun() extends Value
case class ManyVals(values: List[Value]) extends Value // List of multiple values

// Case classes representing different types of expressions
case class ManyExprs(exprs: List[Expr]) extends Expr // List of multiple expressions
case class Plus(left: Expr, right: Expr) extends Expr // Addition operation
case class Not(expr: Expr) extends Expr // Negation operation
case class Count(left: Expr, right: Expr) extends Expr // Count operation (not implemented)

// Object containing the interpreter logic
object Interpreter {
  // Evaluation function for expressions
  def eval(expr: Expr): Value = expr match {
    // If the expression is already a value, return it as is
    case v: Value => v

    // Evaluation for a list of expressions
    case ManyExprs(exprs) =>
      val values = exprs.map(eval) // Evaluate each expression in the list
      if (values.contains(ERROR)) ERROR // If any evaluation results in an error, return error
      else ManyVals(values) // Otherwise, return the list of evaluated values

    // Evaluation for the addition operation
    case Plus(e1, e2) =>
      (eval(e1), eval(e2)) match {
        case (VeryHappy(), _) => VeryHappy() // If the left operand is VeryHappy, result is VeryHappy
        case (_, VeryHappy()) => VeryHappy() // If the right operand is VeryHappy, result is VeryHappy
        case (Cry(), v2) => v2 // If the left operand is Cry, result is the right operand
        case (v1, Cry()) if v1 == Happy() || v1 == Stun() => Cry() // If the right operand is Cry and the left is Happy or Stun, result is Cry
        case (v1, v2) if v1 != ERROR && v2 != VeryHappy() && v2 != Cry() => v1 // If neither operand is an error, result is the left operand
        case (ManyVals(values), v2) =>
          // Evaluate addition for each value in the list with the right operand
          val newValues = values.map(v => eval(Plus(v, v2)))
          if (newValues.contains(ERROR)) ERROR // If any result is an error, return error
          else ManyVals(newValues) // Otherwise, return the list of new values
        case (v1, v2) =>
          // If either operand is an error, return error
          if (v1 == ERROR || v2 == ERROR) ERROR
          else v1
      }

    // Evaluation for the negation operation
    case Not(e1) =>
      eval(e1) match {
        case Stun() => Sleepy() // If operand is Stun, result is Sleepy
        case Sleepy() => Stun() // If operand is Sleepy, result is Stun
        case Happy() | VeryHappy() => Cry() // If operand is Happy or VeryHappy, result is Cry
        case Cry() => VeryHappy() // If operand is Cry, result is VeryHappy
        case ManyVals(values) =>
          // If operand is a list of values, reduce the list using addition and apply negation to the result
          if (values.length >= 2) {
            val reducedValue = values.reduce((v1, v2) => eval(Plus(v1, v2)))
            eval(Not(reducedValue))
          } else ERROR // If less than 2 values, return error
        case other => other match {
          // Apply specific negations to individual values
          case Happy() => Cry()
          case VeryHappy() => Cry()
          case Cry() => VeryHappy()
          case Sleepy() => Stun()
          case Stun() => Sleepy()
          case _ => ERROR // If unknown value, return error
        }
      }

    // Count operation is not implemented, always returns error
    case Count(e1, e2) => ERROR
  }

  // Define ERROR as Cry
  val ERROR: Value = Cry()
}
