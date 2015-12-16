package de.digitalstep.timetrack.ui

import java.time.{LocalTime, LocalDate}

import de.digitalstep.timetrack.ui.converters.{LocalTimeStringConverter, LocalDateStringConverter}

import scalafx.beans.value.ObservableValue
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.{TableColumn, TableView}
import scalafx.util.StringConverter

class WorkUnitTable(buffer: ObservableBuffer[WorkUnitAdapter]) extends TableView[WorkUnitAdapter] {

  type CellValueFactory[T] = TableColumn.CellDataFeatures[WorkUnitAdapter, String] => ObservableValue[T, T]
  type WorkUnitColumn = TableColumn[WorkUnitAdapter, String]

  columns ++= Seq(
    dateColumn("Date", _.value.dayProperty),
    timeColumn("From", _.value.fromProperty),
    timeColumn("To", _.value.toProperty),
    stringColumn("Description", _.value.descriptionProperty)
  )

  private[this] def dateColumn(text: String, property: CellValueFactory[LocalDate]) =
    column(text, property, LocalDateStringConverter.short)

  private[this] def timeColumn(text: String, property: CellValueFactory[LocalTime]) =
    column(text, property, LocalTimeStringConverter.short)


  private[this] def stringColumn(_text: String, observable: CellValueFactory[String]): WorkUnitColumn = new WorkUnitColumn {
    text = _text
    cellValueFactory = x ⇒ observable(x)
  }

  private[this] def column[T](_text: String,
                              observable: CellValueFactory[T],
                              converter: StringConverter[T]): WorkUnitColumn =
    new WorkUnitColumn {
      text = _text
      cellValueFactory = x ⇒ StringBinding(observable(x), converter)
    }

  items = buffer
}
