package de.digitalstep.timetrack

import java.time.LocalDate

import de.digitalstep.timetrack.persistence._
import de.digitalstep.timetrack.test.PropertySpecification
import org.scalacheck.Arbitrary
import collection.mutable

class RepositorySpecification extends PropertySpecification {

  import persistence.Generators._
  import persistence.Generators.Implicits._

  private[this] def repository(data: List[Day]): Repository = new Repository(new Storage {

    val sections: mutable.ListBuffer[Section] = mutable.ListBuffer() ++ data

    def add(date: LocalDate, t: Task): Storage = {
      sections += Day(date, Seq(t))
      this
    }

    def save(): Unit = {}
  })

  implicit val arbitraryWorkUnits = Arbitrary(for {
    day ← genDay
    task ← genTask
  } yield WorkUnit(day, task))

  property("findAll") {
    forAll { data: List[Day] ⇒
      repository(data).findAll.map(_.toTask) should contain theSameElementsAs (data flatMap (_.tasks))
    }
  }

  property("findDays") {
    forAll { data: List[Day] ⇒
      repository(data).findDays.keys should contain theSameElementsAs data.filter(_.tasks.nonEmpty).map(_.date).toSet
    }
  }

  property("add") {
    forAll { (data: List[Day], workUnit: WorkUnit) ⇒
      repository(data).add(Seq(workUnit)).findAll should contain(workUnit)
    }
  }

}
