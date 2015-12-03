package de.digitalstep.timetrack.persistence

import java.time.{LocalDate, LocalTime}

import de.digitalstep.timetrack.persistence.Generators._
import org.scalacheck.Arbitrary._
import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.Gen._

private[timetrack] object Generators extends Generators

trait Generators {

  val genTime = for {
    hour ← choose(0, 23)
    minute ← choose(0, 59)
  } yield LocalTime.of(hour, minute)

  val genDate = for {
    year ← choose(2010, 2030)
    month ← choose(1, 12)
    day ← choose(1, 28)
  } yield LocalDate.of(year, month, day)

  val genDescription = arbitrary[String]

  val genTask = for {
    from ← genTime
    to ← genTime
    desc ← genDescription
  } yield Task(from, to, desc)

  def genDay: Gen[Day] = genDay(genTask)

  def genDay(task: Gen[Task]): Gen[Day] = for {
    date ← genDate
    tasks ← containerOf[List, Task](task)
  } yield Day(date, tasks)

  val genDays = containerOf[List, Day](genDay)

  val genComment = for {
    text ← arbitrary[String]
  } yield Comment(text)

  val genData = containerOf[List, Section](oneOf(genDay, genComment))

  val genInputText = for {
    data ← genData
  } yield InputText(data)

  object Implicits {
    implicit val arbitraryInput = Arbitrary(genInputText)
    implicit val arbitraryData = Arbitrary(genData)
    implicit val arbitraryDays = Arbitrary(genDays)
    implicit val arbitraryDate = Arbitrary(genDate)
    implicit val arbitraryTask = Arbitrary(genTask)
  }

}
