package de.digitalstep.timetrack.ui

import scalafx.Includes._
import scalafx.scene.Node
import scalafx.scene.control.{ButtonType, Dialog}
import scalafx.scene.layout.GridPane

class EditWorkUnitDialog(val workUnit: WorkUnitAdapter) extends Dialog[WorkUnitAdapter] with WorkUnitInput {
  title = "Add Entry"

  dialogPane().content = new GridPane {
    hgap = 5
    vgap = 5

    val constraints: Seq[(Node, Node)] = Seq(
      (dayLabel, dayInput),
      (fromLabel, fromText),
      (toLabel, toText),
      (descriptionLabel, descriptionText)
    )

    for ((y, (left, right)) ← (0 to constraints.size) zip constraints) {
      GridPane.setConstraints(left, 0, y)
      GridPane.setConstraints(right, 1, y)
    }

    children = Seq(
      dayLabel, dayInput,
      fromLabel, fromText,
      toLabel, toText,
      descriptionLabel, descriptionText
    )
  }

  dialogPane().buttonTypes = Seq(ButtonType.OK, ButtonType.Cancel)
  resultConverter = {
    case ButtonType.OK ⇒ workUnit
    case _ ⇒ null
  }


}