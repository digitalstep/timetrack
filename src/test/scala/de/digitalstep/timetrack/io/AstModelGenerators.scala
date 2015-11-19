package de.digitalstep.timetrack.io

import java.time.{LocalDate, LocalTime}

import org.scalacheck.Arbitrary._
import org.scalacheck.Gen._

object AstModelGenerators extends AstModelGenerators

trait AstModelGenerators {

  val genTime = for {
    hour ← choose(0, 23)
    minute ← choose(0, 59)
  } yield LocalTime.of(hour, minute)

  val genDate = for {
    year ← choose(2010, 2030)
    month ← choose(1, 12)
    day ← choose(1, 28)
  } yield LocalDate.of(year, month, day)

  val genTask = for {
    from ← genTime
    to ← genTime
    desc ← arbitrary[String]
  } yield Task(from, to, desc)

  val genDay = for {
    date ← genDate
    tasks ← containerOf[List, Task](genTask)
  } yield Day(date, tasks)

  val genComment = for {
    text ← arbitrary[String]
  } yield Comment(text)

  val genData = containerOf[List, Section](genDay)

}
