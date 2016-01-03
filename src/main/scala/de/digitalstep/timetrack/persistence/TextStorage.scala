package de.digitalstep.timetrack.persistence

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
    () ⇒ InputParser(path).days,
    Serializer(path))

}

private[persistence] class TextStorage(
                                        input: () ⇒ Iterable[Day],
                                        output: Serializer)
  extends Storage with LazyLogging {

  val dayMap: mutable.Map[LocalDate, Set[Task]] = mutable.Map() ++
    input().map(d ⇒ d.date → d.tasks.toSet)

  def days: Iterable[Day] = for ((date, tasks) ← dayMap) yield Day(date, tasks.toSeq)

  def findDay(date: LocalDate): Option[Day] = dayMap.get(date).map(t ⇒ Day(date, t.toSeq))

  def save(): Unit = {
    logger.debug("Saving to {}", output)
    InputText(dayMap.toSeq.map(entry ⇒ Day(entry._1, entry._2.toSeq)))
    output.serialize(
      InputText(
        for ((date, tasks) ← dayMap) yield Day(date, tasks.toSeq)
      )
    )

    dayMap.clear()
    dayMap ++= input().map(d ⇒ d.date → d.tasks.toSet)
  }

  def add(date: LocalDate, task: Task): Storage = {
    dayMap.put(date, dayMap.getOrElse(date, Set()) + task)
    logger.debug("Storage now holds {} sections", new Integer(dayMap.keys.size))
    this
  }
}