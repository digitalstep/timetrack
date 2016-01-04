package de.digitalstep.timetrack.ui

import scalafx.Includes._
import scalafx.collections.ObservableBuffer
import scalafx.scene.Node
import scalafx.scene.control.ButtonType.{Cancel, OK}
import scalafx.scene.control.Dialog
import scalafx.scene.layout.GridPane

object EditWorkUnitDialog {
  def create(workUnits: ObservableBuffer[WorkUnitAdapter]): Unit = edit(WorkUnitAdapter()) { x ⇒
    workUnits.add(x)
    workUnits.sort((a, b) ⇒ a.get > b.get)
  }

  def update(adapter: WorkUnitAdapter, workUnits: ObservableBuffer[WorkUnitAdapter]): Unit =
    edit(adapter) { x ⇒
      workUnits.replaceAll(x, x)
    }

  private[this] def edit(adapter: WorkUnitAdapter)(fn: WorkUnitAdapter ⇒ Unit): Unit = {
    val dialog = new EditWorkUnitDialog(adapter)
    dialog.showAndWait() match {
      case Some(_) ⇒ fn(dialog.getResult)
      case _ ⇒ println("Cancelled")
    }
  }
}

class EditWorkUnitDialog(val workUnit: WorkUnitAdapter) extends Dialog[WorkUnitAdapter] with WorkUnitInput {
  title = "Add Entry"

  dialogPane().content = new GridPane {
    hgap = 5
    vgap = 5

    val nodes: Seq[(Node, Node)] = Seq(
      (dayLabel, dayInput),
      (fromLabel, fromText),
      (toLabel, toText),
      (descriptionLabel, descriptionText)
    )

    for ((y, (left, right)) ← (0 to nodes.size) zip nodes) {
      GridPane.setConstraints(left, 0, y)
      GridPane.setConstraints(right, 1, y)
    }

    children = nodes.flatMap(tuple ⇒ Seq(tuple._1, tuple._2))
  }

  dialogPane().buttonTypes = Seq(OK, Cancel)
  resultConverter = {
    case OK ⇒ workUnit
    case _ ⇒ null
  }


}