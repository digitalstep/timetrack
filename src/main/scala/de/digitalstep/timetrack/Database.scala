package de.digitalstep.timetrack

import java.time.LocalDate

import de.digitalstep.timetrack.io.{Task, Day, FileDatabase}

object Database {
  def apply(): Database = FileDatabase()
}

abstract class Database {

  def days: Seq[Day]

  def add(day: LocalDate, task: Task): Database

  def add(workUnit: WorkUnit): Database = {
    add(workUnit.date, workUnit.toTask)
    this
  }

  def add(x: Traversable[WorkUnit]): Database = x.foldLeft(this)(_ add _)

  def findAll: Iterable[WorkUnit] = {
    val tasks = for (d ← days; t ← d.tasks) yield WorkUnit(d, t)
    tasks.toSeq.sorted.reverse
  }

  def findDays: Map[LocalDate, Iterable[WorkUnit]] = findAll groupBy (_.date)

}
