package de.digitalstep.timetrack.io

import java.time._

import org.parboiled2.ParserInput

import scala.collection.mutable
import scala.io.Source.fromInputStream
import scala.language.implicitConversions
import scala.reflect.io.Path.string2path
import scala.reflect.io.{File, Path}

private[io] object TextStorage {
  private[this] implicit def path2string(path: Path): ParserInput = fromInputStream(File(path).inputStream()).mkString

  private[this] val path = sys.props("user.home") / ".digitalstep" / "Zeiterfassung.txt"

  def apply(): TextStorage = apply(path)

  def apply(path: Path): TextStorage = new TextStorage(InputParser(path).sections)
}

private[io] class TextStorage(input: ⇒ Iterable[Section]) extends Storage {

  val sections: mutable.ListBuffer[Section] = mutable.ListBuffer() ++ input

  def findDay(date: LocalDate): Option[Day] =
    sections.
      find(x ⇒ x.isInstanceOf[Day] && x.asInstanceOf[Day].date == date).
      map(_.asInstanceOf[Day])

  def save(): Unit = {
    sections foreach println
  }

  def add(date: LocalDate, task: Task): Storage = {
    sections += Day(date, task :: findDay(date).toList.flatMap(_.tasks))
    this
  }

}