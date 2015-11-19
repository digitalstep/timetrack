package de.digitalstep.timetrack.io

import java.time._

import de.digitalstep.timetrack.{Database, WorkUnit}
import org.parboiled2.ParserInput

import scala.collection.mutable
import scala.language.implicitConversions
import scala.reflect.io.File
import scala.reflect.io.Path.string2path

object FileDatabase {
  private[this] def file2string(file: File): String = io.Source.fromInputStream(file.inputStream()).mkString

  private[this] val file = File(sys.props("user.home") / ".digitalstep" / "Zeiterfassung.txt")

  def apply(): FileDatabase = apply(file)

  def apply(file: File): FileDatabase = new FileDatabase(
    new DataReader {
      override def sections = InputParser(file2string(file)).sections
    })
}

class FileDatabase(input: DataReader) extends Database {

  private[this] val sections: mutable.Seq[Section] = mutable.Seq() ++ input.sections

  private[this] val workUnits: mutable.SortedSet[WorkUnit] = mutable.SortedSet(load().toSeq: _*)

  private[this] def createWorkUnit(d: Day, t: Task) = WorkUnit(
    from = LocalDateTime.of(d.date, t.from),
    to = LocalDateTime.of(d.date, t.to),
    description = t.name
  )

  private[this] def mergeWorkUnit(workUnit: WorkUnit) = {
    val day = sections.find {
      case Day(x, _) if x == workUnit.date ⇒ true
      case _ ⇒ false
    }.asInstanceOf[Option[Day]] getOrElse Day(workUnit.date, Seq())

    val newDay = day.copy(
      tasks = Task(workUnit.from.toLocalTime, workUnit.to.toLocalTime, workUnit.description) :: day.tasks.toList
    )

    val index = sections.indexOf(day)
    sections(index) = newDay

  }


  def load(): Iterable[WorkUnit] = {
    val days = sections.filter(_.isInstanceOf[Day]).asInstanceOf[Seq[Day]]

    for (d ← days; t ← d.tasks) yield createWorkUnit(d, t)
  }

  def save(): Unit = {
    sections foreach println
  }

  def add(x: WorkUnit*): Unit = add(x.toSeq)

  def add(x: Traversable[WorkUnit]): Unit = workUnits ++= x

  def findAll: Iterable[WorkUnit] = load().toSeq.sorted.reverse

  def findDays: Map[LocalDate, Iterable[WorkUnit]] = findAll.groupBy(_.date)
}