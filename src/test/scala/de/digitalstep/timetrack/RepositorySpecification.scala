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

    val map: mutable.HashMap[LocalDate, List[Task]] = mutable.HashMap(data.map(d ⇒ d.date → d.tasks.toList): _*)

    def days: Iterable[Day] = for ((date, tasks) ← map) yield Day(date, tasks.toSeq)

    def add(date: LocalDate, t: Task): Storage = {
      map.put(date, t :: map.getOrElse(date, Nil))
      this
    }

    def delete(date: LocalDate, t: Task): Storage = {
      map.put(date, map(date).filterNot(x ⇒ x == t))
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
      val actual = repository(data).findAll.map(_.toTask)
      val expected = data.flatMap(_.tasks)
      actual should contain theSameElementsAs expected
    }
  }

  property("findDays") {
    forAll { data: List[Day] ⇒
      repository(data).findDays.keys should contain theSameElementsAs data.filter(_.tasks.nonEmpty).map(_.date).toSet
    }
  }

  property("findTasks") {
    val data: List[Day] = List(
      Day(date(2016, 1, 1), Seq(
        Task(time(9, 0), time(10, 0), "Development"),
        Task(time(9, 0), time(10, 0), "Development"))),
      Day(date(2016, 1, 1), Seq(
        Task(time(9, 0), time(10, 0), "Development"),
        Task(time(9, 0), time(10, 0), "other")))
    )

    repository(data).findTasks("Dev") should contain theSameElementsAs Seq("Development")
    repository(data).findTasks("other") should contain theSameElementsAs Seq("other")
    repository(data).findTasks("other2") shouldBe empty
  }

  property("add") {
    forAll { (data: List[Day], workUnit: WorkUnit) ⇒
      repository(data).add(Seq(workUnit)).findAll should contain(workUnit)
    }
  }

}
