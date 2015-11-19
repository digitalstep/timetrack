package de.digitalstep.timetrack.io

import de.digitalstep.timetrack.test.PropertySpecification
import org.scalatest.Matchers._

class FileDatabaseSpecification extends PropertySpecification {

  private[this] def fileDatabase(data: List[Section]) =
    new FileDatabase(
      new DataReader {
        def sections = data
      }
    )

  import AstModelGenerators.genData

  property("FileDatabase.findAll") {
    forAll(genData) {
      data ⇒ {
        val expected: List[(Day, Task)] = data.
          filter(_.isInstanceOf[Day]).
          map(_.asInstanceOf[Day]).
          flatMap(d ⇒ d.tasks.map(t ⇒ (d, t)))

        val actual = fileDatabase(data).findAll

        actual should have size expected.size
      }
    }


  }
}
