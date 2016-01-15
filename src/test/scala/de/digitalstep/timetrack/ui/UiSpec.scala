package de.digitalstep.timetrack.ui

import java.util.function.Consumer
import javafx.scene.Scene
import javafx.stage.Stage

import com.typesafe.scalalogging.LazyLogging
import org.scalatest.{BeforeAndAfter, FlatSpec}
import org.testfx.api.FxToolkit._

import scalafx.Includes._
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.BorderPane

class UiSpec extends FlatSpec with BeforeAndAfter with LazyLogging {

  registerPrimaryStage()
  showStage()

  before {
    setupStage(new Consumer[Stage] {
      override def accept(t: Stage): Unit = t.setScene(new Scene(new BorderPane {
        val out = new Label("out")
        val in = new Button("In") {
          onAction = () ⇒ out.text = "Done"
        }
        center = out
        bottom = in
      }.delegate))
    })
    showStage()
  }

  after {
    cleanupStages()
  }

  "The output"

}
