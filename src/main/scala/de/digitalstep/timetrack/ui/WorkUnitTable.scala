package de.digitalstep.timetrack.ui

import java.time.{LocalDate, LocalTime}

import de.digitalstep.timetrack.ui.converters.{LocalDateStringConverter, LocalTimeStringConverter}

import scalafx.Includes._
import scalafx.beans.value.ObservableValue
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.{ContextMenu, MenuItem, TableColumn, TableView}
import scalafx.util.StringConverter

class WorkUnitTable(buffer: ObservableBuffer[WorkUnitAdapter]) extends TableView[WorkUnitAdapter] with ColumnFactory {
  items = buffer

  columns ++= Seq(
    dateColumn("Date", _.value.dayProperty),
    timeColumn("From", _.value.fromProperty),
    timeColumn("To", _.value.toProperty),
    stringColumn("Description", _.value.descriptionProperty)
  )

  contextMenu = new ContextMenu(editItem, removeItem)

  private[this] lazy val editItem = new MenuItem {
    text = "Edit"
    onAction = () ⇒ EditWorkUnitDialog.update(selectionModel.value.selectedItems.head, buffer)
  }

  private[this] lazy val removeItem = new MenuItem("Remove")

}

trait ColumnFactory {
  type CellValueFactory[T] = TableColumn.CellDataFeatures[WorkUnitAdapter, String] => ObservableValue[T, T]

  type WorkUnitColumn = TableColumn[WorkUnitAdapter, String]

  protected[this] def dateColumn(text: String, property: CellValueFactory[LocalDate]) =
    column(text, property, LocalDateStringConverter.short)

  protected[this] def timeColumn(text: String, property: CellValueFactory[LocalTime]) =
    column(text, property, LocalTimeStringConverter.short)

  protected[this] def stringColumn(_text: String, observable: CellValueFactory[String]): WorkUnitColumn = new WorkUnitColumn {
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


}