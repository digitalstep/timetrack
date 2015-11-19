package de.digitalstep.timetrack.ui

import de.digitalstep.timetrack.WorkUnit

import scalafx.beans.property.{ObjectProperty, StringProperty}

class WorkUnitAdapter(workUnit: WorkUnit) {

  import workUnit._

  val descriptionProperty = StringProperty(description)
  val dayProperty = ObjectProperty(date)
  val fromProperty = ObjectProperty(from.toLocalTime)
  val toProperty = ObjectProperty(to.toLocalTime)

  def get: WorkUnit = WorkUnit(
    dayProperty.value,
    fromProperty.value,
    toProperty.value,
    descriptionProperty.value
  )

}

object WorkUnitAdapter {
  def apply(): WorkUnitAdapter = new WorkUnitAdapter(WorkUnit.empty)

  def apply(x: WorkUnit): WorkUnitAdapter = new WorkUnitAdapter(x)
}