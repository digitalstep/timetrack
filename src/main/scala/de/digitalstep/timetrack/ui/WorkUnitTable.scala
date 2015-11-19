package de.digitalstep.timetrack.ui

import scalafx.beans.value.ObservableValue
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.{TableColumn, TableView}
import scalafx.util.StringConverter

class WorkUnitTable(buffer: ObservableBuffer[WorkUnitAdapter]) extends TableView[WorkUnitAdapter] {

  type CellValueFactory[T] = TableColumn.CellDataFeatures[WorkUnitAdapter, String] => ObservableValue[T, T]
  type WorkUnitColumn = TableColumn[WorkUnitAdapter, String]

  def column(_text: String, observable: CellValueFactory[String]): WorkUnitColumn = new WorkUnitColumn {
    text = _text
    cellValueFactory = x ⇒ observable(x)
  }

  def column[T](_text: String,
                observable: CellValueFactory[T],
                converter: StringConverter[T]): WorkUnitColumn = new WorkUnitColumn {
    text = _text
    cellValueFactory = x ⇒ StringBinding(observable(x), converter)
  }

  columns ++= Seq(
    column(
      "Date",
      _.value.dayProperty,
      LocalDateStringConverter.shortFormatStyle
    ),
    column(
      "From",
      _.value.fromProperty,
      LocalTimeStringConverter.short
    ),
    column(
      "To",
      _.value.toProperty,
      LocalTimeStringConverter.short
    ),
    column(
      "Description",
      _.value.descriptionProperty
    )
  )

  items = buffer
}
