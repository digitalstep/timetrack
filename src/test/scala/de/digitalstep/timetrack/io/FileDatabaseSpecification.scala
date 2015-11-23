package de.digitalstep.timetrack.io

import java.time.{LocalDate, LocalTime}

import de.digitalstep.timetrack.WorkUnit
import de.digitalstep.timetrack.test.PropertySpecification

class FileDatabaseSpecification extends PropertySpecification {

  import AstModelGenerators.genData

  property("FileDatabase.findAll") {
    forAll(genData) {
      data ⇒ {
        val daysWithTasksOnly: List[(Day, Task)] = data.
          filter(_.isInstanceOf[Day]).
          map(_.asInstanceOf[Day]).
          flatMap(d ⇒ d.tasks.map(t ⇒ (d, t)))

        val actual = new FileDatabase(data).findAll

        actual should have size daysWithTasksOnly.size
      }
    }
  }

  property("FileDatabase.add") {
    forAll(genData) {
      data ⇒ {
        val workUnit = WorkUnit(LocalDate.of(2015, 12, 1), LocalTime.of(8, 0), LocalTime.of(10, 0), "asdf")
        val actual = new FileDatabase(data) add workUnit
        actual.findAll should contain(workUnit)
      }
    }
  }
}
