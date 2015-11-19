package de.digitalstep.timetrack.ui

import java.time.LocalDate
import scalafx.scene.control.{TextFormatter, DatePicker, Label, TextField}

trait WorkUnitInput {
  val workUnit: WorkUnitAdapter

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

}
