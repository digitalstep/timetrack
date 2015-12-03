package de.digitalstep.timetrack.persistence

import java.time.LocalDate
import java.time.LocalTime.{of ⇒ Time}

import de.digitalstep.timetrack.test.UnitSpec

class InputParserSpec extends UnitSpec {

  "A comment" should "be parsed" in {
    new InputParser("#asdf").comment.run().get shouldEqual Comment("asdf")
  }

  it should "be contained in a list of sections" in {
    new InputParser("#asdf").all.run().get shouldEqual InputText(Seq(Comment("asdf")))
  }


  import CompleteFiles._

  "A simple timesheet" should "be parsed" in {
    val days: InputText = new InputParser(simple).all.run().get

    days.sections.head shouldEqual Day(
      LocalDate.of(2015, 11, 16),
      Seq(
        Task(Time(9, 45), Time(12, 0), "Coding")
      )
    )
  }

  it should "handle and store comments" in {
    val days: InputText = new InputParser(withComment).all.run().get

    days.sections.head shouldEqual Comment("This is a comment")
  }

  it should "handle and store comments in between other top level elements" in {
    val days: InputText = new InputParser(multipleSectionsWithComment).all.run().get
    val s1 :: comment :: s2 :: Nil = days.sections.toList

    s1 shouldEqual Day(
      LocalDate.of(2015, 11, 16),
      Seq(
        Task(Time(9, 45), Time(12, 0), "Coding"),
        Task(Time(13, 0), Time(18, 0), "Coding2")
      )
    )
    comment shouldEqual Comment(" This is another comment")
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

  val withComment = "#This is a comment"

  val multipleSectionsWithComment =
    """2015-11-16
      | 9:45 - 12:00   Coding
      |13:00 - 18:00   Coding2
      |
      |# This is another comment
      |2015-11-17
      |10:00 - 11:00   Coding
      | """.stripMargin

}