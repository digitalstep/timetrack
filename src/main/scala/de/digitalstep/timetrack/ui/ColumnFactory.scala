package de.digitalstep.timetrack.ui

import java.time.{Duration, LocalTime, LocalDate}

import de.digitalstep.timetrack.ui.converters.{LocalDurationStringConverter, LocalTimeStringConverter, LocalDateStringConverter}

import scalafx.beans.value.ObservableValue
import scalafx.scene.control.TableColumn
import scalafx.util.StringConverter

private[ui] trait ColumnFactory[S] extends Messages {
  type CellValueFactory[S, T] = TableColumn.CellDataFeatures[S, String] => ObservableValue[T, T]

  type WorkUnitColumn = TableColumn[S, String]

  protected[this] def dateColumn(text: Symbol, property: CellValueFactory[S, LocalDate]) =
    column(text, property, LocalDateStringConverter.short)

  protected[this] def timeColumn(text: Symbol, property: CellValueFactory[S, LocalTime]) =
    column(text, property, LocalTimeStringConverter.short)

  protected[this] def durationColumn(text: Symbol, property: CellValueFactory[S, Duration]) =
    column(text, property, LocalDurationStringConverter.short)

  protected[this] def stringColumn(_text: Symbol, observable: CellValueFactory[S, String]) =
    new WorkUnitColumn {
      text = _text
      cellValueFactory = x ⇒ observable(x)
    }

  private[this] def column[T](_text: Symbol,
                              observable: CellValueFactory[S, T],
                              converter: StringConverter[T]): WorkUnitColumn =
    new WorkUnitColumn {
      text = _text
      cellValueFactory = x ⇒ StringBinding(observable(x), converter)
    }


}
