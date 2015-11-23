package de.digitalstep.timetrack.io

import java.time.LocalDate

import com.typesafe.scalalogging.LazyLogging
import de.digitalstep.timetrack.{WorkUnit, Database}
import de.digitalstep.timetrack.test.PropertySpecification
import org.scalacheck.Gen._

class DatabaseSpecification extends PropertySpecification {

  private[this] def database(data: List[Day]): Database = new Database with LazyLogging {
    logger.debug(s"New database with ${data.length} entries")

    override def days: Seq[Day] = data

    override def add(date: LocalDate, task: Task): Database = {
      val day: Day = Day(date, Seq(task))
      database(day :: data)
    }
  }

  import AstModelGenerators._

  val genDays = containerOf[List, Day](genDay)

  property("Database.findDays") {
    forAll(genDays) { data ⇒
      database(data).findDays.keys should contain theSameElementsAs data.filter(_.tasks.nonEmpty).map(_.date).toSet
    }
  }

  property("Database.add") {
    forAll(genDays, genDay) { (data, day) ⇒
      day.tasks.toList match {
        case t :: _ ⇒ database(data).add(day.date, t).findAll should contain(WorkUnit(day, t))
        case Nil ⇒ database(data).findAll should not contain day
      }
    }
  }

}
