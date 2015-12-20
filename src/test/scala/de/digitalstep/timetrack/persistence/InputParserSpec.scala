package de.digitalstep.timetrack.persistence

import java.time.LocalDate
import java.time.LocalTime.{of ⇒ Time}

import de.digitalstep.timetrack.test.UnitSpec

class InputParserSpec extends UnitSpec {

  import CompleteFiles._

  "A simple timesheet" should "be parsed" in {
    val days: InputText = new InputParser(simple).all.run().get

    days.days.head shouldEqual Day(
      LocalDate.of(2015, 11, 16),
      Seq(
        Task(Time(9, 45), Time(12, 0), "Coding")
      )
    )
  }

  it should "handle multiple top level elements" in {
    val days: InputText = new InputParser(multipleSectionsWithComment).all.run().get
    val s1 :: s2 :: Nil = days.days.toList

    s1 shouldEqual Day(
      date(2015, 11, 16),
      Seq(
        Task(time(9, 45), time(12, 0), "Coding"),
        Task(time(13, 0), time(18, 0), "Coding2")
      )
    )
    s2 shouldEqual Day(
      LocalDate.of(2015, 11, 17),
      Seq(
        Task(Time(10, 0), Time(11, 0), "Coding")
      )
    )
  }

}

object CompleteFiles {

  val simple = "2015-11-16\n 9:45 - 12:00   Coding\n\n"

  val multipleSectionsWithComment =
    """2015-11-16
      | 9:45 - 12:00   Coding
      |13:00 - 18:00   Coding2
      |
      |2015-11-17
      |10:00 - 11:00   Coding
      | """.stripMargin

}