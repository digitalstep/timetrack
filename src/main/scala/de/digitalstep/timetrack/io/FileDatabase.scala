package de.digitalstep.timetrack.io

import java.time._

import de.digitalstep.timetrack.{Database, WorkUnit}
import org.parboiled2.ParserInput

import scala.collection.mutable
import scala.language.implicitConversions
import scala.reflect.io.{Path, File}
import scala.reflect.io.Path.string2path
import scala.io.Source.fromInputStream

object FileDatabase {
  private[this] implicit def path2string(path: Path): ParserInput = fromInputStream(File(path).inputStream()).mkString

  private[this] val path = sys.props("user.home") / ".digitalstep" / "Zeiterfassung.txt"

  def apply(): FileDatabase = apply(path)

  def apply(path: Path): FileDatabase = new FileDatabase(InputParser(path).sections)
}

class FileDatabase(input: ⇒ Iterable[Section]) extends Database {

  private[this] val sections: mutable.ListBuffer[Section] = mutable.ListBuffer() ++ input

  private[this] def mergeWorkUnit(workUnit: WorkUnit) = {
    val day = sections.find {
      case Day(x, _) if x == workUnit.date ⇒ true
      case _ ⇒ false
    }.asInstanceOf[Option[Day]] getOrElse Day(workUnit.date, List())

    val newDay = day.copy(
      tasks = Task(workUnit.from.toLocalTime, workUnit.to.toLocalTime, workUnit.description) :: day.tasks.toList
    )

    val index = sections.indexOf(day)
    sections(index) = newDay

  }

  def days: Seq[Day] = sections.filter(_.isInstanceOf[Day]).asInstanceOf[Seq[Day]]

  def findDay(date: LocalDate): Option[Day] =
    sections.
      find(x ⇒ x.isInstanceOf[Day] && x.asInstanceOf[Day].date == date).
      map(_.asInstanceOf[Day])

  def toTask(workUnit: WorkUnit): Task = workUnit.toTask

  def save(): Unit = {
    sections foreach println
  }

  def add(date: LocalDate, task: Task): Database = {
    sections += Day(date, task :: findDay(date).toList.flatMap(_.tasks))
    this
  }

}