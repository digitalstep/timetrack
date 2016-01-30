package de.digitalstep.timetrack.ui

import scalafx.scene.control.TableView

class DayTable(actionProvider: ActionContext) extends TableView[DayAdapter] with ColumnFactory[DayAdapter] {
  columns ++= Seq(
    dateColumn('day, _.value.dayProperty),
    timeColumn('from, _.value.fromProperty),
    timeColumn('to, _.value.toProperty),
    durationColumn('break, _.value.breakProperty),
    durationColumn('hours, _.value.hoursProperty)
  )
}
