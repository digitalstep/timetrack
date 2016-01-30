package de.digitalstep.timetrack.ui

import scalafx.Includes._
import scalafx.scene.control.SelectionMode.MULTIPLE
import scalafx.scene.control._

abstract class WorkUnitTable(actionProvider: ActionContext)
  extends TableView[WorkUnitAdapter] with ColumnFactory[WorkUnitAdapter] {

  columns ++= Seq(
    dateColumn('date, _.value.dayProperty),
    timeColumn('from, _.value.fromProperty),
    timeColumn('to, _.value.toProperty),
    stringColumn('description, _.value.descriptionProperty)
  )

  selectionModel.value.selectionMode = MULTIPLE

  contextMenu = new ContextMenu(
    new MenuItem('edit) {
      onAction = () ⇒ EditWorkUnitDialog.update(selectedItems.head, actionProvider)
    },
    new MenuItem('remove) {
      onAction = () ⇒ actionProvider.workUnits --= selectedItems
    })

  private[this] def selectedItems = selectionModel.value.selectedItems

}

