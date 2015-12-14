package de.digitalstep.timetrack.persistence

import java.time.{LocalDate, LocalTime}

import org.scalacheck.Arbitrary._
import org.scalacheck.Gen._
import org.scalacheck.{Arbitrary, Gen}

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
    x ← genTime
    y ← genTime
    desc ← genDescription
  } yield if (y isBefore x) Task(y, x, "a") else Task(x, y, "a")

  def genDay: Gen[Day] = genDay(genTask)

  def genDay(task: Gen[Task]): Gen[Day] = for {
    date ← genDate
    tasks ← containerOf[List, Task](task)
  } yield Day(date, tasks)

  val genDayMap = mapOf[LocalDate, Seq[Task]](for {
    date ← genDate
    tasks ← containerOf[Seq, Task](genTask)
  } yield (date, tasks))

  val genDays = genDayMap.map {
    _.toList.map { f ⇒
      Day(f._1, f._2)
    }
  }

  val genInputText = genDays.map(InputText.apply)

  object Implicits {
    implicit val arbitraryInput = Arbitrary(genInputText)
    implicit val arbitraryDays = Arbitrary(genDays)
    implicit val arbitraryDate = Arbitrary(genDate)
    implicit val arbitraryTask = Arbitrary(genTask)
  }

}
