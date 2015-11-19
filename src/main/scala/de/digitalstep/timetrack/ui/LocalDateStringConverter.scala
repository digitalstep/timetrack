package de.digitalstep.timetrack.ui

import java.time.LocalDate
import java.time.format.{DateTimeFormatter, FormatStyle}

import scalafx.util.StringConverter

object LocalDateStringConverter {

  def shortFormatStyle: LocalDateStringConverter = new LocalDateStringConverter(FormatStyle.SHORT)

  def localDateFromString(style: FormatStyle)(localDate: String) =
    LocalDate.parse(localDate, DateTimeFormatter.ofLocalizedDate(style))
}

class LocalDateStringConverter(style: FormatStyle) extends StringConverter[LocalDate] {

  private[this] val formatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDate(style)

  override def fromString(string: String) = LocalDate.parse(string, formatter)

  override def toString(t: LocalDate): String = t.format(formatter)

}
