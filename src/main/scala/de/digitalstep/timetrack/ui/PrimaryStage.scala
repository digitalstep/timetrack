package de.digitalstep.timetrack.ui

import javafx.event.{ActionEvent, EventHandler}

import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Tab, TabPane, ToolBar}
import scalafx.scene.layout.BorderPane

object PrimaryStage {

  def apply(actionProvider: ActionProvider): JFXApp.PrimaryStage =
    new JFXApp.PrimaryStage {
      title = "timetrack"
      scene = new Scene {
        root = new BorderPane {

          top = toolbar()

          center = new TabPane {
            tabs = Seq(
              new Tab {
                text = "Erfassung"
                content = workUnitTable()
              }
            )
          }
        }
      }

      private[this] def workUnitTable() = new WorkUnitTable(actionProvider) {
        items = actionProvider.workUnits
      }

      private[this] def toolbar() = new ToolBar {
        content = Seq(
          new Button("+") {
            onAction = actionProvider.createWorkUnit
          }
        )
      }
    }


}
