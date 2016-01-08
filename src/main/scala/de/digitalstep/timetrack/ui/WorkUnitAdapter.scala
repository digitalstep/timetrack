package de.digitalstep.timetrack.ui

import java.time.{LocalTime, LocalDate}
import java.time.temporal.ChronoUnit

import de.digitalstep.timetrack.WorkUnit

import scalafx.beans.property.{ObjectProperty, StringProperty}

class WorkUnitAdapter() {

  val descriptionProperty = StringProperty("")
  val dayProperty = ObjectProperty(LocalDate.now())
  val fromProperty = ObjectProperty(LocalTime.now.truncatedTo(ChronoUnit.MINUTES))
  val toProperty = ObjectProperty(LocalTime.now.truncatedTo(ChronoUnit.MINUTES))


  def this(workUnit: WorkUnit) {
    this()
    import workUnit._
    descriptionProperty.value = description
    dayProperty.value = from.toLocalDate
    fromProperty.value = from.toLocalTime.truncatedTo(ChronoUnit.MINUTES)
    toProperty.value = to.toLocalTime.truncatedTo(ChronoUnit.MINUTES)
  }

  def get = WorkUnit(
    dayProperty.value,
    fromProperty.value,
    toProperty.value,
    descriptionProperty.value
  )

}

object WorkUnitAdapter {
  def apply(): WorkUnitAdapter = new WorkUnitAdapter()

  def apply(x: WorkUnit): WorkUnitAdapter = {
    val adapter = apply()
    import adapter._
    import x._
    descriptionProperty.value = description
    dayProperty.value = from.toLocalDate
    fromProperty.value = from.toLocalTime.truncatedTo(ChronoUnit.MINUTES)
    toProperty.value = to.toLocalTime.truncatedTo(ChronoUnit.MINUTES)
    adapter
  }
}