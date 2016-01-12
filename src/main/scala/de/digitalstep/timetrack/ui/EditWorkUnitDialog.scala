package de.digitalstep.timetrack.ui

import org.controlsfx.control.textfield.TextFields

import scalafx.Includes._
import scalafx.collections.ObservableBuffer
import scalafx.scene.Node
import scalafx.scene.control.ButtonType.{Cancel, OK}
import scalafx.scene.control.Dialog
import scalafx.scene.layout.GridPane

object EditWorkUnitDialog {
  def create(taskSuggestions: String ⇒ Iterable[String], workUnits: ObservableBuffer[WorkUnitAdapter]): Unit =
    edit(WorkUnitAdapter(), taskSuggestions) { x ⇒
      workUnits.add(x)
      workUnits.sort((a, b) ⇒ a.get > b.get)
    }

  def update(adapter: WorkUnitAdapter, actionProvider: ActionProvider): Unit =
    edit(adapter, actionProvider.suggest) { x ⇒
      actionProvider.workUnits.replaceAll(x, x)
    }

  private[this] def edit(adapter: WorkUnitAdapter, taskSuggestions: String ⇒ Iterable[String])
                        (fn: WorkUnitAdapter ⇒ Unit): Unit = {
    val dialog = new EditWorkUnitDialog(adapter, taskSuggestions)
    dialog.showAndWait() match {
      case Some(_) ⇒ fn(dialog.getResult)
      case _ ⇒ println("Cancelled")
    }
  }
}

class EditWorkUnitDialog(val workUnit: WorkUnitAdapter, taskSuggestions: String ⇒ Iterable[String])
  extends Dialog[WorkUnitAdapter] with WorkUnitInput {
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

    TextFields.bindAutoCompletion(descriptionText,
      collection.JavaConversions.asJavaCollection(taskSuggestions("")))

    children = nodes.flatMap(tuple ⇒ Seq(tuple._1, tuple._2))
  }

  dialogPane().buttonTypes = Seq(OK, Cancel)
  resultConverter = {
    case OK ⇒ workUnit
    case _ ⇒ null
  }

}