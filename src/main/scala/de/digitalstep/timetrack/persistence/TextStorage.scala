package de.digitalstep.timetrack.persistence

import java.io.OutputStream
import java.time._

import com.typesafe.scalalogging.LazyLogging
import org.parboiled2.ParserInput

import scala.collection.mutable
import scala.io.Source.fromInputStream
import scala.language.implicitConversions
import scala.reflect.io.Path.string2path
import scala.reflect.io.{File, Path}

private[persistence] object TextStorage {
  private[this] implicit def path2string(path: Path): ParserInput = fromInputStream(File(path).inputStream()).mkString

  private[this] val path = sys.props("user.home") / ".digitalstep" / "Zeiterfassung.txt"

  def apply(): TextStorage = apply(path)

  def apply(path: Path): TextStorage = new TextStorage(
    () ⇒ InputParser(path).sections,
    FileSerializer(path))

}

private[persistence] class TextStorage(
                                        input: () ⇒ Iterable[Section],
                                        output: Serializer) extends Storage with LazyLogging {

  val sections: mutable.ListBuffer[Section] = mutable.ListBuffer() ++ input()

  def findDay(date: LocalDate): Option[Day] =
    sections.
      find(x ⇒ x.isInstanceOf[Day] && x.asInstanceOf[Day].date == date).
      map(_.asInstanceOf[Day])

  def save(): Unit = {
    sections foreach output.serialize
    sections.clear()
    sections ++= input()
  }

  def add(date: LocalDate, task: Task): Storage = {
    val index = sections.indexWhere(s ⇒ s.isInstanceOf[Day] && s.asInstanceOf[Day].date == date)
    logger.debug("Found entry with date {} at index {}", date, new Integer(index))
    index match {
      case -1 ⇒ sections += Day(date, Seq(task))
      case x ⇒ sections(x) = Day(date, task :: sections(x).asInstanceOf[Day].tasks.toList)
    }
    this
  }
}