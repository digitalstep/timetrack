package de.digitalstep.timetrack

import java.time.LocalDate

import com.typesafe.scalalogging.LazyLogging
import de.digitalstep.timetrack.persistence._

import scala.collection.mutable

object Repository {

  def apply(): Repository = new Repository(Storage())

  sealed trait Change

  case class Add(workUnit: WorkUnit) extends Change

  case class Delete(workUnit: WorkUnit) extends Change

}

class Repository(storage: Storage) extends LazyLogging {

  import Repository._

  private[this] val listeners: mutable.ArrayBuffer[Change ⇒ Unit] = mutable.ArrayBuffer() += (c ⇒ {
    logger.debug("Saving because of {}", c)
    storage.save()
  })

  def findAll: Iterable[WorkUnit] = {
    val tasks = for (d ← storage.days; t ← d.tasks) yield WorkUnit(d, t)
    tasks.toSeq.sorted.reverse
  }

  def findDays: Map[LocalDate, Iterable[WorkUnit]] = findAll.groupBy(_.date)

  def findDaysSorted: Seq[(LocalDate, Iterable[WorkUnit])] = findDays.toSeq.sortBy(_._1).reverse

  def add(x: Traversable[WorkUnit]): Repository = x.foldLeft(this) {
    _ add _
  }

  def findAllTasks: Iterable[String] = findAll map (_.description)

  def findTasks(prefix: String): Iterable[String] = findAllTasks filter (_ startsWith prefix)

  def add(workUnit: WorkUnit): Repository = {
    logger.debug("Adding {}", workUnit)
    def notify(listener: Change ⇒ Unit) = listener(Add(workUnit))

    storage.add(workUnit.date, workUnit.toTask)
    listeners foreach notify
    this
  }

  def delete(x: Traversable[WorkUnit]): Repository = x.foldLeft(this) {
    _ delete _
  }

  def delete(workUnit: WorkUnit): Repository = {
    logger.debug("Removing {}", workUnit)
    def notify(listener: Change ⇒ Unit) = listener(Delete(workUnit))

    storage.delete(workUnit.date, workUnit.toTask)
    listeners foreach notify
    this
  }

  /**
    * Adds a new listener to the repository
    *
    * @param op operation to be executed on changes
    * @return a new Subscription
    */
  def onChange(op: Change ⇒ Unit): Subscription = new Subscription(op)

  /**
    * Can be cancelled to remove a listener from the repository.
    *
    * @param op operation to be executed on changes
    */
  class Subscription private[Repository](op: Change ⇒ Unit) {
    listeners += op

    def cancel() = listeners -= op
  }

}
