package de.digitalstep.timetrack.ui

import scalafx.application.JFXApp
import scalafx.collections.ObservableBuffer
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Tab, TabPane, ToolBar}
import scalafx.scene.layout.BorderPane

object PrimaryStage {

  def apply(provider: ActionProvider, workUnits: ObservableBuffer[WorkUnitAdapter]): JFXApp.PrimaryStage =
    new JFXApp.PrimaryStage {
      title = "timetrack"
      scene = new Scene {
        root = new BorderPane {

          top = toolbar(provider)

          center = new TabPane {
            tabs = Seq(
              new Tab {
                text = "Erfassung"
                content = workUnitTable(provider, workUnits)
              }
            )
          }
        }
      }
    }

  private[this] def workUnitTable(provider: ActionProvider,
                                  workUnits: ObservableBuffer[WorkUnitAdapter]): WorkUnitTable =
    new WorkUnitTable(workUnits, provider)

  private[this] def toolbar(provider: ActionProvider) = new ToolBar {
    content = Seq(
      new Button {
        text = "+"
        onAction = provider.createWorkUnit
      }
    )
  }


}
