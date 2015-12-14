package de.digitalstep.timetrack

import java.time.LocalDate

import com.typesafe.scalalogging.LazyLogging
import de.digitalstep.timetrack.persistence._

import scala.collection.mutable

object Repository {

  def apply(): Repository = new Repository(Storage())

}

class Repository(storage: Storage) extends LazyLogging {

  sealed trait Change

  case class Add(workUnit: WorkUnit) extends Change

  class Subscription(op: Change ⇒ Unit) {
    def cancel() = listeners -= op
  }

  private[this] def saveOnChange(c: Change): Unit = storage.save()

  private[this] val listeners: mutable.ArrayBuffer[Change ⇒ Unit] = mutable.ArrayBuffer(saveOnChange)

  def add(workUnit: WorkUnit): Repository = {
    logger.debug("Adding {}", workUnit)
    def notify(listener: Change ⇒ Unit) = listener(Add(workUnit))

    storage.add(workUnit.date, workUnit.toTask)
    listeners foreach notify
    this
  }

  def onChange(op: Change ⇒ Unit): Subscription = {
    listeners += op
    new Subscription(op)
  }

  def add(x: Traversable[WorkUnit]): Repository = x.foldLeft(this) {
    _ add _
  }

  def findAll: Iterable[WorkUnit] = {
    val tasks = for (d ← storage.days; t ← d.tasks) yield WorkUnit(d, t)
    tasks.toSeq.sorted.reverse
  }

  def findDays: Map[LocalDate, Iterable[WorkUnit]] = findAll groupBy (_.date)

}
