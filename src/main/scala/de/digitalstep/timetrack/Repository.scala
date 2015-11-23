package de.digitalstep.timetrack

import java.time.LocalDate

import de.digitalstep.timetrack.io._

object Repository {
  def apply(): Repository = new Repository(Storage())
}

class Repository(storage: Storage) {

  def add(workUnit: WorkUnit): Repository = {
    storage.add(workUnit.date, workUnit.toTask)
    this
  }

  def add(x: Traversable[WorkUnit]): Repository = x.foldLeft(this)(_ add _)

  def findAll: Iterable[WorkUnit] = {
    val tasks = for (d ← storage.days; t ← d.tasks) yield WorkUnit(d, t)
    tasks.toSeq.sorted.reverse
  }

  def findDays: Map[LocalDate, Iterable[WorkUnit]] = findAll groupBy (_.date)

}
