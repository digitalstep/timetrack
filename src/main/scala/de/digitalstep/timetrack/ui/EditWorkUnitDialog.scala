package de.digitalstep.timetrack.ui

import scalafx.Includes._
import scalafx.scene.control.{ButtonType, Dialog}
import scalafx.scene.layout.GridPane

class EditWorkUnitDialog(val workUnit: WorkUnitAdapter) extends Dialog[WorkUnitAdapter] with WorkUnitInput {
  title = "Add Entry"

  dialogPane().content = new GridPane {
    hgap = 5
    vgap = 5

    GridPane.setConstraints(dayLabel, 0, 0)
    GridPane.setConstraints(dayInput, 1, 0)
    GridPane.setConstraints(fromLabel, 0, 1)
    GridPane.setConstraints(fromText, 1, 1)
    GridPane.setConstraints(toLabel, 0, 2)
    GridPane.setConstraints(toText, 1, 2)
    GridPane.setConstraints(descriptionLabel, 0, 3)
    GridPane.setConstraints(descriptionText, 1, 3)

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