package de.digitalstep.timetrack

import java.time.{LocalTime, Duration, LocalDate, LocalDateTime}

import de.digitalstep.timetrack.persistence.{Task, Day}

/**
  * A time frame with a label describing what work has been done.
  *
  * @param from beginning of the time frame
  * @param to end of time frame
  * @param description what has been done
  */
case class WorkUnit(from: LocalDateTime, to: LocalDateTime, description: String) extends Ordered[WorkUnit] {

  override def compare(that: WorkUnit): Int = this.from compareTo that.from

  def date = LocalDate.from(from)

  def duration = Duration.between(from, to)

  def toTask: Task = Task(from.toLocalTime, to.toLocalTime, description)

}

object WorkUnit {
  def empty: WorkUnit = WorkUnit(LocalDateTime.now(), LocalDateTime.now(), "")

  def apply(day: Day, task: Task): WorkUnit = WorkUnit(
    from = LocalDateTime.of(day.date, task.from),
    to = LocalDateTime.of(day.date, task.to),
    description = task.name
  )

  def apply(day: LocalDate, from: LocalTime, to: LocalTime, description: String): WorkUnit =
    WorkUnit(
      LocalDateTime.of(day, from),
      LocalDateTime.of(day, to),
      description
    )

}