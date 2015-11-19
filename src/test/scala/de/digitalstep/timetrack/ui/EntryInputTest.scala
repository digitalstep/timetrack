package de.digitalstep.timetrack.ui

import java.time.LocalDate.{now ⇒ today}
import java.time.LocalDateTime.{of ⇒ dateTime}
import java.time.LocalTime.{of ⇒ at}
import java.util.Locale

import de.digitalstep.timetrack.WorkUnit

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.event.ActionEvent
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.GridPane

object EntryInputTest extends JFXApp with WorkUnitInput {
  Locale.setDefault(Locale.GERMANY)

  val workUnit = WorkUnitAdapter(WorkUnit(today(), at(9, 0), at(12, 0), "Development"))

  lazy val update = new Button {
    text = "Update"
    onAction = (e: ActionEvent) ⇒ output.text = workUnit.get.toString
  }

  lazy val output = new Label {
    text
  }

  stage = new JFXApp.PrimaryStage {
    title = "timetrack"
    scene = new Scene {
      root = new GridPane {
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
        GridPane.setConstraints(update, 1, 4)
        GridPane.setConstraints(output, 1, 4)

        children = Seq(
          dayLabel, dayInput,
          fromLabel, fromText,
          toLabel, toText,
          descriptionLabel, descriptionText,
          update, output
        )
      }
    }
  }


}
