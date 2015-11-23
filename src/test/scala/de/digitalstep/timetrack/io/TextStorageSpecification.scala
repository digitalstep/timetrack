package de.digitalstep.timetrack.io

import java.time.LocalDate

import de.digitalstep.timetrack.test.PropertySpecification

class TextStorageSpecification extends PropertySpecification {

  import Generators.Implicits._

  property("TextStorage.sections") {
    forAll { data: List[Section] ⇒
      new TextStorage(data).sections should contain theSameElementsAs data
    }
  }

  property("TextStorage.findDay") {
    forAll { data: List[Section] ⇒
      data foreach {
        case day@Day(date, _) ⇒ new TextStorage(data).findDay(date).map(_.date) should be(Some(date))
        case _ ⇒ // nothing to verify
      }
    }
  }

  property("TextStorage.add") {
    forAll { (data: List[Day], date: LocalDate, task: Task) ⇒
      new TextStorage(data).add(date, task).days.map(_.date) should contain(date)
    }
  }

}
