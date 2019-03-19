package org.timspence.scalaplayground.lenses

import cats.syntax.flatMap._
import cats.effect.{ExitCode, IO, IOApp}
import monocle.Lens
import monocle.macros.GenLens
import monocle.function.all._

case class University(departments: List[Department])

object University {

  val departments: Lens[University, List[Department]] = GenLens[University](_.departments)

}

case class Department(lecturers: List[Lecturer])

object Department {

  val lecturers: Lens[Department, List[Lecturer]] = GenLens[Department](_.lecturers)

}

case class Lecturer(firstName: String, surname: String, interests: Set[String])

object Lecturer {

  val firstName: Lens[Lecturer, String] = GenLens[Lecturer](_.firstName)

  val surname: Lens[Lecturer, String] = GenLens[Lecturer](_.surname)

  val interests: Lens[Lecturer, Set[String]] = GenLens[Lecturer](_.interests)

}

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    import University._
    import Department._
    import Lecturer._

    val simon = Lecturer("Simon", "Peyton Jones", Set("haskell", "fp"))
    val philip = Lecturer("Philip", "Wadler", Set("haskell", "fp"))
    val timothy = Lecturer("Timothy", "Gowers", Set("analysis", "combinatorics"))
    val ben = Lecturer("Ben", "Green", Set("number theory"))
    val cs = Department(List(simon, philip))
    val maths = Department(List(timothy, ben))
    val uni = University(List(cs, maths))

    val lens = departments composeTraversal each composeLens lecturers composeOptional headOption composeLens firstName

    IO(println(lens.set("database corruption")(uni))) >> IO.pure(ExitCode.Success)
  }
}
