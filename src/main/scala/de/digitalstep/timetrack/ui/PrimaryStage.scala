package de.digitalstep.timetrack.ui

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.collections.ObservableBuffer
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Tab, TabPane, ToolBar}
import scalafx.scene.layout.BorderPane

object PrimaryStage {

  def apply(workUnits: ObservableBuffer[WorkUnitAdapter]): JFXApp.PrimaryStage =
    new JFXApp.PrimaryStage {
      title = "timetrack"
      scene = new Scene {
        root = new BorderPane {

          top = toolbar(workUnits)

          center = new TabPane {
            tabs = Seq(
              new Tab {
                text = "Erfassung"
                content = workUnitTable(workUnits)
              }
            )
          }
        }
      }
    }

  private[this] def workUnitTable(workUnits: ObservableBuffer[WorkUnitAdapter]): WorkUnitTable =
    new WorkUnitTable(workUnits)

  private[this] def toolbar(workUnits: ObservableBuffer[WorkUnitAdapter]) = new ToolBar {
    content = Seq(
      new Button {
        text = "+"
        onAction = { () ⇒ EditWorkUnitDialog.create(workUnits) }
      }
    )
  }


}
