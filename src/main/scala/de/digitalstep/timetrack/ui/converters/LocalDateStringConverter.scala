package de.digitalstep.timetrack.ui.converters

import java.time.LocalDate
import java.time.format.{DateTimeFormatter, FormatStyle}

import scalafx.util.StringConverter

object LocalDateStringConverter {
  def short: LocalDateStringConverter = new LocalDateStringConverter(FormatStyle.SHORT)
}

class LocalDateStringConverter private(style: FormatStyle) extends StringConverter[LocalDate] {

  private[this] val formatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDate(style)

  override def fromString(string: String) = LocalDate.parse(string, formatter)

  override def toString(t: LocalDate): String = t.format(formatter)

}
