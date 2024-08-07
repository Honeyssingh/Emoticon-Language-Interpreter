// Define the abstract syntax for expressions and values
sealed trait Expr
sealed trait Value extends Expr

// Different types of expressions
case class ManyExprs(exprs: List[Expr]) extends Expr // A list of expressions
case class Plus(left: Expr, right: Expr) extends Expr // Addition of two expressions
case class Not(expr: Expr) extends Expr // Negation of an expression
case class Count(left: Expr, right: Expr) extends Expr // Count operation between two expressions

// Different types of values
case object Cry extends Value // Represents a crying emoticon
case object Happy extends Value // Represents a happy emoticon
case object VeryHappy extends Value // Represents a very happy emoticon
case object Sleepy extends Value // Represents a sleepy emoticon
case object Stun extends Value // Represents a stunned emoticon
case class ManyVals(values: List[Value]) extends Value // A list of values

object Interpreter {
  def eval(expr: Expr): Value = expr match {
    // Base case: if the expression is already a value, return it
    case v: Value => v

    // Evaluate a list of expressions
    case ManyExprs(exprs) =>
      val evaluated = exprs.map(eval) // Evaluate each expression in the list
      if (evaluated.contains(ERROR)) ERROR // If any expression evaluates to ERROR, return ERROR
      else ManyVals(evaluated.collect { case v: Value => v }) // Otherwise, collect the evaluated values into a ManyVals

    // Evaluate the addition of two expressions
    case Plus(left, right) =>
      (eval(left), eval(right)) match {
        // If the left expression is a list of values, add the right value to each one
        case (ManyVals(values), r) =>
          val results = values.map(v => eval(Plus(v, r)))
          if (results.contains(ERROR)) ERROR // If any addition results in ERROR, return ERROR
          else ManyVals(results.collect { case v: Value => v }) // Otherwise, collect the results into a ManyVals

        // If either expression is VeryHappy, the result is VeryHappy
        case (VeryHappy, _) => VeryHappy
        case (_, VeryHappy) => VeryHappy
        // If the left expression is Cry, return the right value
        case (Cry, r) => r
        // If the right expression is Cry and the left is Happy or Stun, return Cry
        case (v, Cry) if v == Happy || v == Stun => Cry
        // If both expressions are neither ERROR nor special cases, return the left value
        case (v1, v2) if v1 != ERROR && v2 != VeryHappy && v2 != Cry => v1
        // If none of the above cases match, return ERROR
        case _ => ERROR
      }

    // Evaluate the negation of an expression
    case Not(expr) =>
      eval(expr) match {
        // If the expression is Stun, return Sleepy
        case Stun => Sleepy
        // If the expression is Sleepy, return Stun
        case Sleepy => Stun
        // If the expression is Happy or VeryHappy, return Cry
        case Happy | VeryHappy => Cry
        // If the expression is Cry, return VeryHappy
        case Cry => VeryHappy
        // If the expression is a list of values, reduce them using addition
            case ManyVals(values) =>
              if (values.isEmpty) ERROR // Added this line to handle empty list case
              else values.reduceLeft((acc, v) => eval(Plus(acc, v)))
        // If none of the above cases match, return ERROR
        case _ => ERROR
      }

    // Evaluate the count of occurrences of the right value in the left list of values
    case Count(left, right) =>
      (eval(left), eval(right)) match {
        // If the left expression is a list of values, filter them by the right value
        case (ManyVals(values), r) => ManyVals(values.filter(_ == r))
        // If the right expression is a list of values, filter them by the left value
        case (v, ManyVals(values)) => ManyVals(values.filter(_ == v))
        // If none of the above cases match, return ERROR
        case _ => ERROR
      }
  }

  val ERROR: Value = Cry // Define the ERROR value as Cry
}
