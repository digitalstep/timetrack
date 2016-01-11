package de.digitalstep.timetrack.ui

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.collections.ObservableBuffer
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Tab, TabPane, ToolBar}
import scalafx.scene.layout.BorderPane

object PrimaryStage {

  def apply(workUnits: ObservableBuffer[WorkUnitAdapter],
            taskSuggestions: String ⇒ Iterable[String]): JFXApp.PrimaryStage =
    new JFXApp.PrimaryStage {
      title = "timetrack"
      scene = new Scene {
        root = new BorderPane {

          top = toolbar(workUnits, taskSuggestions)

          center = new TabPane {
            tabs = Seq(
              new Tab {
                text = "Erfassung"
                content = workUnitTable(workUnits, taskSuggestions)
              }
            )
          }
        }
      }
    }

  private[this] def workUnitTable(workUnits: ObservableBuffer[WorkUnitAdapter],
                                  taskSuggestions: String ⇒ Iterable[String]): WorkUnitTable =
    new WorkUnitTable(workUnits, taskSuggestions)

  private[this] def toolbar(workUnits: ObservableBuffer[WorkUnitAdapter], taskSuggestions: String ⇒ Iterable[String]) =
    new ToolBar {
      content = Seq(
        new Button {
          text = "+"
          onAction = { () ⇒ EditWorkUnitDialog.create(taskSuggestions, workUnits) }
        }
      )
    }


}
