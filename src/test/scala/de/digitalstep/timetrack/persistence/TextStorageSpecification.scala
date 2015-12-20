package de.digitalstep.timetrack.persistence

import java.io.{ByteArrayOutputStream, OutputStream}
import java.time.{LocalTime, LocalDate}

import de.digitalstep.timetrack.test.PropertySpecification

class TextStorageSpecification extends PropertySpecification {

  import Generators.Implicits._

  property("days") {
    forAll { data: List[Day] ⇒
      val dateSet = new TextStorage(() ⇒ data, mockSerializer).days.map(_.date).toSet
      val expected = data.map(_.date).toSet
      dateSet should contain theSameElementsAs expected
    }
  }

  property("findDay") {
    forAll { data: InputText ⇒
      data.days foreach {
        case day@Day(date, _) ⇒ new TextStorage(() ⇒ data.days, mockSerializer).findDay(date).map(_.date) should be(Some(date))
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

  val initialTestData = List(
    Day(testDate1, Seq(
      testTask1)),
    Day(date(2015, 11, 30), Seq(
      testTask1,
      testTask2)))

  def tested = new TextStorage(
    () ⇒ {
      initialTestData
    },
    mockSerializer)

  property("add") {
    forAll { (data: List[Day], date: LocalDate, task: Task) ⇒
      val x = new TextStorage(() ⇒ InputText(data).days, mockSerializer)
      x.add(date, task)
      x.days.map(_.date) should contain(date)
      x.days should have size (date :: data.map(_.date)).toSet.size
    }
  }

  property("save") {
    tested.save()

    mockSerializer.out.toString("UTF-8") shouldBe
      """2015-12-01
        |09:00 - 10:00   Test
        |
        |2015-11-30
        |09:00 - 10:00   Test
        |11:00 - 12:00   Test""".stripMargin +
        "\n\n"

    tested.days should be(initialTestData.filter(_.isInstanceOf[Day]))
  }

}
