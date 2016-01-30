package de.digitalstep.timetrack.ui

import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout.BorderPane

object PrimaryStage {

  def apply(actionProvider: ActionContext): JFXApp.PrimaryStage =
    new JFXApp.PrimaryStage {
      title = "timetrack"
      scene = new Scene {
        root = new BorderPane {

          top = toolbar()

          center = new TabPane {
            tabs = Seq(
              new Tab {
                text = Message('journal)
                content = workUnitTable()
              },
              new Tab {
                text = YearMonth.now().getMonth.getDisplayName(TextStyle.FULL, Locale.getDefault) +
                  " " + YearMonth.now().getYear
                content = dayTable(YearMonth.now())
              }
            )
          }
        }
      }

      private[this] def dayTable(month: YearMonth) = new DayTable(actionProvider) {
        items = actionProvider.days.filter(
          x ⇒ x.dayProperty.value.getMonth == month.getMonth && x.dayProperty.value.getYear == month.getYear)
      }

      private[this] def workUnitTable() = new WorkUnitTable(actionProvider) {
        items = actionProvider.workUnits
      }

      private[this] def toolbar() = new ToolBar {
        content = Seq(
          new Button("+") {
            onAction = () ⇒ EditWorkUnitDialog.create(actionProvider)
          }
        )
      }
    }
}
