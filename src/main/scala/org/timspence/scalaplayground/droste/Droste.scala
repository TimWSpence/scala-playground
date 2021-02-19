package org.timspence.scalaplayground.droste

import cats._
import cats.implicits._
import higherkindness.droste._
// import higherkindness.droste.scheme._
// import higherkindness.droste.scheme.zoo._
import higherkindness.droste.data._

object Playground {
  val natCoalgebra: Coalgebra[Option, BigDecimal] =
    Coalgebra(n => if (n > 0) Some(n - 1) else None)

  val fibAlgebra: CVAlgebra[Option, BigDecimal] = CVAlgebra {
    case Some(r1 :< Some(r2 :< _)) => r1 + r2
    case Some(_ :< None)           => 1
    case None                      => 0
    case x                         => sys.error(s"Unreachable $x")
  }

  val fib: BigDecimal => BigDecimal = scheme.ghylo(fibAlgebra.gather(Gather.histo), natCoalgebra.scatter(Scatter.ana))

  implicit val embed: Embed[Option, BigDecimal] = new Embed[Option, BigDecimal] {
    def algebra: Algebra[Option, BigDecimal] =
      Algebra {
        case Some(n) => n + 1
        case None    => 0
      }
  }

  val factCoalg: Coalgebra[Option, BigDecimal] =
    Coalgebra(n => if (n > 0) Some(n - 1) else None)

  val factAlg: RAlgebra[BigDecimal, Option, BigDecimal] = RAlgebra {
    case Some((m, n)) => (m + 1) * n
    case None         => 1
  }

  val fact: BigDecimal => BigDecimal = scheme.ghylo(
    factAlg.gather(Gather.para),
    factCoalg.scatter(Scatter.ana)
  )

  sealed trait Tree[A]
  case class Leaf[A]()                                          extends Tree[A]
  case class Branch[A](value: A, left: Tree[A], right: Tree[A]) extends Tree[A]

  object Tree {

    implicit def treeAlgebra[A]: Algebra[TreeF[A, *], Tree[A]] =
      Algebra {
        case LeafF()          => Leaf()
        case BranchF(a, l, r) => Branch(a, l, r)
      }

    implicit def treeCoalgebra[A]: Coalgebra[TreeF[A, *], Tree[A]] =
      Coalgebra {
        case Leaf()          => LeafF()
        case Branch(a, l, r) => BranchF(a, l, r)
      }

    implicit def basisForTree[A]: Basis[TreeF[A, *], Tree[A]] = Basis.Default(treeAlgebra, treeCoalgebra)
  }

  sealed trait TreeF[A, B]
  case class LeafF[A, B]()                              extends TreeF[A, B]
  case class BranchF[A, B](value: A, left: B, right: B) extends TreeF[A, B]

  object TreeF {

    implicit def traverseForTreeT[X]: Traverse[TreeF[X, *]] =
      new Traverse[TreeF[X, *]] {

        override def foldLeft[A, B](fa: TreeF[X, A], b: B)(f: (B, A) => B): B =
          fa match {
            case LeafF() => b
            case BranchF(_, l, r) =>
              val y = f(b, l)
              f(y, r)
          }

        override def foldRight[A, B](fa: TreeF[X, A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
          fa match {
            case LeafF() => lb
            case BranchF(_, l, r) =>
              val y = f(l, lb)
              f(r, y)
          }

        override def traverse[G[_]: Applicative, A, B](fa: TreeF[X, A])(f: A => G[B]): G[TreeF[X, B]] =
          fa match {
            case LeafF()          => Applicative[G].pure(LeafF())
            case BranchF(x, l, r) => (f(l), f(r)).mapN(BranchF(x, _, _))
          }

      }
  }

}
