package de.digitalstep.timetrack.ui

import java.text.NumberFormat
import java.util.Locale

import scala.language.implicitConversions
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.{Slider, TextField, TextFormatter}
import scalafx.scene.layout.{BorderPane, Region, VBox}
import scalafx.util.converter.FormatStringConverter

object TextFormatterDemo extends JFXApp {

  val slider = new Slider(0, 10000, 1000)

  val textField = {
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)
    val converter = new FormatStringConverter[Number](currencyFormat)
    new TextField {
      textFormatter = new TextFormatter(converter) {
        value <==> slider.value
      }
      maxWidth = 140
      maxHeight = Region.USE_COMPUTED_SIZE
    }
  }

  stage = new PrimaryStage {
    scene = new Scene {
      title = "TextFormatter Demo"
      root = new BorderPane {
        center = textField
        top = slider
      }
    }
  }

//  slider.requestFocus()
}