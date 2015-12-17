package de.digitalstep.timetrack.ui

import scalafx.Includes._
import scalafx.scene.Node
import scalafx.scene.control.ButtonType.{Cancel, OK}
import scalafx.scene.control.Dialog
import scalafx.scene.layout.GridPane

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