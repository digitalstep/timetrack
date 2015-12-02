package de.digitalstep.timetrack

import java.time.LocalDate

import de.digitalstep.timetrack.io._
import de.digitalstep.timetrack.test.PropertySpecification
import org.scalacheck.Arbitrary
import collection.mutable

class DatabaseSpecification extends PropertySpecification {

  import io.Generators._
  import io.Generators.Implicits._

  private[this] def database(data: List[Day]): Repository = new Repository(
    new Storage {

      val sections: mutable.ListBuffer[Section] = mutable.ListBuffer() ++ data

      def add(date: LocalDate, t: Task): Storage = {
        sections += Day(date, Seq(t))
        this
      }

    }
  )

  implicit val arbitraryWorkUnits = Arbitrary(for {
    day ← genDay
    task ← genTask
  } yield WorkUnit(day, task))

  property("Database.findAll") {
    forAll { data: List[Day] ⇒
      database(data).findAll.map(_.toTask) should contain theSameElementsAs (data flatMap (_.tasks))
    }
  }

  property("Database.findDays") {
    forAll { data: List[Day] ⇒
      database(data).findDays.keys should contain theSameElementsAs data.filter(_.tasks.nonEmpty).map(_.date).toSet
    }
  }

  property("Database.add") {
    forAll { (data: List[Day], workUnit: WorkUnit) ⇒
      database(data).add(Seq(workUnit)).findAll should contain(workUnit)
    }
  }

}
