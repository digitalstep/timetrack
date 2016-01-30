package de.digitalstep.timetrack.ui

import java.time.temporal.ChronoUnit
import java.time.{Duration, LocalTime, LocalDate}
import java.time.Duration.ZERO

import de.digitalstep.timetrack.WorkUnit

import scalafx.beans.property.ObjectProperty

object DayAdapter {
  def apply(date: LocalDate, workUnits: Iterable[WorkUnit]): DayAdapter = new DayAdapter(
    date,
    workUnits.min.from.toLocalTime,
    workUnits.max.to.toLocalTime,
    workUnits.map(_.duration).fold(ZERO) {
      _ plus _
    },
    Duration.between(
      workUnits.min.from.toLocalTime,
      workUnits.max.to.toLocalTime
    ) minus
      workUnits.map(_.duration).fold(ZERO) {
        _ plus _
      }
  )
}

class DayAdapter(private val date: LocalDate,
                 private val from: LocalTime,
                 private val to: LocalTime,
                 private val hours: Duration,
                 private val break: Duration) {

  val dayProperty = ObjectProperty(date)
  val fromProperty = ObjectProperty(from)
  val toProperty = ObjectProperty(to)
  val hoursProperty = ObjectProperty(hours)
  val breakProperty = ObjectProperty(break)

}
