package de.digitalstep.timetrack.ui

import java.time.LocalDate

import de.digitalstep.timetrack.ui.converters.LocalTimeStringConverter
import org.controlsfx.control.textfield.TextFields
import scala.collection.JavaConversions._

import scalafx.scene.control.{DatePicker, Label, TextField, TextFormatter}

trait WorkUnitInput {

  val workUnit: WorkUnitAdapter
  val suggest: String ⇒ Iterable[String]

  import workUnit._

  lazy val dayLabel = new Label("Day")
  lazy val dayInput = new DatePicker(LocalDate.now()) {
    value <==> dayProperty
  }

  lazy val fromLabel = new Label("From")

  lazy val fromText = new TextField {
    textFormatter = new TextFormatter(LocalTimeStringConverter.short) {
      value <==> fromProperty
    }

  }

  lazy val toLabel = new Label("To")
  lazy val toText = new TextField {
    textFormatter = new TextFormatter(LocalTimeStringConverter.short) {
      value <==> toProperty
    }

  }

  lazy val descriptionLabel = new Label("Description")
  lazy val descriptionText = new TextField {
    text <==> descriptionProperty
  }

  TextFields.bindAutoCompletion(descriptionText, asJavaCollection(suggest("")))

}
