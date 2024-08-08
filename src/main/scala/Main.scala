
// Extends expr
sealed trait Expr {
  def eval: Value = {
    this match {
        case v: Value => v
        case ManyExprs(lst) => ManyVals(lst.map(_.eval))
        case Plus(e1, e2) => 
            val v1 = e1.eval
            val v2 = e2.eval
            (v1, v2) match{
                case (ManyVals(lst), ManyVals(lst2)) => throw new RuntimeException("Cannot add multivals to multivals")
                case (ManyVals(lst), _) => ManyVals(lst.map(x => (Plus(x, v2)).eval))
                case (_, ManyVals(lst)) => ManyVals(lst.map(x => (Plus(v1, x)).eval))
                case (VeryHappy, _) => VeryHappy
                case (_, VeryHappy) => VeryHappy
                case (Cry, _) => v2
                case(Happy, Cry) => Cry
                case(Stun, Cry) => Cry
                case (_, _) if v1 != Cry && v1 != VeryHappy && v2 != Cry && v2 != VeryHappy => v1
                // case (_, v2) =>
                //     if(v2 !)
                case _ => throw new RuntimeException("Error Evaluating in Plus")
            }
        case Not(e) => 
            val v = e.eval
            v match{
                case Stun => Sleepy
                case Sleepy => Stun
                case Happy => Cry
                case VeryHappy => Cry
                case Cry => VeryHappy
                case ManyVals(lst) => 
                    if(lst.length < 2){
                        throw new RuntimeException("Error Evaluating in Not, make sure the list is at least 2 values long")
                    }else{
                        lst.tail.foldLeft(lst.head) {(acc, elem) => (Plus(acc, elem)).eval}
                    }

                case _ => throw new RuntimeException("Error Evaluating in Not")
            }
        case Count(e1, e2) => throw new RuntimeException("There is no implementation instructions for count T.T")
        case _ => throw new RuntimeException("Error Evaluating")
    }
  }
}

sealed trait Value extends Expr

case class ManyExprs(lst: List[Expr]) extends Expr

case class Plus(e1: Expr, e2: Expr) extends Expr

case class Not(e: Expr) extends Expr

case class Count(e1: Expr, e2: Expr) extends Expr

// Extends value

case object Cry extends Value

case object Happy extends Value

case object VeryHappy extends Value

case object Sleepy extends Value

case object Stun extends Value

case class ManyVals(v: List[Value]) extends Value
