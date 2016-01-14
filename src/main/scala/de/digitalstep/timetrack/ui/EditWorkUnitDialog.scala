package de.digitalstep.timetrack.ui

import org.controlsfx.validation.ValidationSupport
import org.controlsfx.validation.Validator.createEmptyValidator

import scala.collection.JavaConversions._
import scalafx.Includes._
import scalafx.beans.property.{ReadOnlyBooleanProperty, BooleanProperty}
import scalafx.scene.Node
import scalafx.scene.control.ButtonType.{Cancel, OK}
import scalafx.scene.control.Dialog
import scalafx.scene.layout.GridPane

object EditWorkUnitDialog {
  def create(actionProvider: ActionContext): Unit =
    edit(WorkUnitAdapter(), actionProvider.suggest) { x ⇒
      actionProvider.workUnits.add(x)
      actionProvider.workUnits.sort((a, b) ⇒ a.get > b.get)
    }

  def update(adapter: WorkUnitAdapter, actionProvider: ActionContext): Unit =
    edit(adapter, actionProvider.suggest) { x ⇒
      actionProvider.workUnits.replaceAll(x, x)
    }

  private[this] def edit(adapter: WorkUnitAdapter, suggest: String ⇒ Iterable[String])
                        (fn: WorkUnitAdapter ⇒ Unit): Unit = {
    val dialog = new EditWorkUnitDialog(adapter, suggest)
    dialog.showAndWait() match {
      case Some(_) ⇒ fn(dialog.getResult)
      case _ ⇒ // nothing to do
    }
  }
}

class EditWorkUnitDialog(val workUnit: WorkUnitAdapter,
                         val suggest: String ⇒ Iterable[String])
  extends Dialog[WorkUnitAdapter] with WorkUnitInput {

  val support = new ValidationSupport

  title = "Add Entry"

  dialogPane().content = Build(createContentPane()) { x ⇒
    support.registerValidator(descriptionText, createEmptyValidator("Description is equired"))
  }

  dialogPane().buttonTypes = Seq(OK, Cancel)

  dialogPane().lookupButton(OK).disable <== new ReadOnlyBooleanProperty(support.invalidProperty())

  resultConverter = {
    case OK ⇒ workUnit
    case _ ⇒ null
  }

  private[this] def createContentPane(): GridPane = {
    new GridPane {
      hgap = 5
      vgap = 5

      private[this] val nodes: Seq[(Node, Node)] = Seq(
        (dayLabel, dayInput),
        (fromLabel, fromText),
        (toLabel, toText),
        (descriptionLabel, descriptionText)
      )

      children = nodes.flatMap(tuple ⇒ Seq(tuple._1, tuple._2))

      for ((y, (left, right)) ← (0 to nodes.size) zip nodes) {
        GridPane.setConstraints(left, 0, y)
        GridPane.setConstraints(right, 1, y)
      }
    }
  }

}