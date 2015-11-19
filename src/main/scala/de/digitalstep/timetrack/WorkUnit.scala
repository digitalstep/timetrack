package de.digitalstep.timetrack

import java.time.{LocalTime, Duration, LocalDate, LocalDateTime}

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

}

object WorkUnit {
  def empty: WorkUnit = WorkUnit(LocalDateTime.now(), LocalDateTime.now(), "")

  def apply(day: LocalDate, from: LocalTime, to: LocalTime, description: String): WorkUnit =
    WorkUnit(
      LocalDateTime.of(day, from),
      LocalDateTime.of(day, to),
      description
    )

}