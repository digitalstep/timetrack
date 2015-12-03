package de.digitalstep.timetrack.io

import java.io.{ByteArrayOutputStream, OutputStream}
import java.time.{LocalTime, LocalDate}

import de.digitalstep.timetrack.test.PropertySpecification

class TextStorageSpecification extends PropertySpecification {

  import Generators.Implicits._

  property("sections") {
    forAll { data: InputText ⇒
      new TextStorage(() ⇒ data.sections, mockSerializer).sections should contain theSameElementsAs data.sections
    }
  }

  property("findDay") {
    forAll { data: InputText ⇒
      data.sections foreach {
        case day@Day(date, _) ⇒ new TextStorage(() ⇒ data.sections, mockSerializer).findDay(date).map(_.date) should be(Some(date))
        case _ ⇒ // nothing to verify
      }
    }
  }

  val testDate1 = date(2015, 12, 1)
  val testDate2 = date(2015, 12, 2)
  val testTask1 = Task(LocalTime.of(9, 0), LocalTime.of(10, 0), "Test")
  val testTask2 = Task(time(11, 0), time(12, 0), "Test")

  private val mockSerializer = new Serializer {
    val out = new ByteArrayOutputStream()

    protected def outputStream(): OutputStream = out

    override protected def close(out: AutoCloseable): Unit = out.close()
  }

  def tested = new TextStorage(
    () ⇒ List(
      Comment("-*-Text-*-"),
      Day(testDate1, Seq(testTask1)),
      Day(date(2015, 11, 30), Seq(testTask1, testTask2))),
    mockSerializer)

  property("add") {
    forAll { (data: List[Day], date: LocalDate, task: Task) ⇒
      new TextStorage(() ⇒ InputText(data).sections, mockSerializer).add(date, task).days.map(_.date) should contain(date)
    }

    tested.add(testDate1, testTask2).days should contain(Day(testDate1, Seq(testTask2, testTask1)))
    tested.add(testDate2, testTask1).days should contain(Day(testDate2, Seq(testTask1)))
  }

  property("save") {
    tested.save()

    mockSerializer.out.toString("UTF-8") shouldBe
      """# -*-Text-*-
        |2015-12-01
        |09:00 - 10:00   Test
        |
        |2015-11-30
        |09:00 - 10:00   Test
        |11:00 - 12:00   Test
        |
        |""".stripMargin
  }

}
