package de.digitalstep.timetrack.ui

import java.time.LocalTime
import java.time.format.{FormatStyle, DateTimeFormatter}

import scalafx.util.StringConverter

class LocalTimeStringConverter(style: FormatStyle) extends StringConverter[LocalTime] {

  private[this] val formatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedTime(style)

  override def fromString(string: String) = LocalTime.parse(string, formatter)

  override def toString(t: LocalTime): String = t.format(formatter)

}

object LocalTimeStringConverter {
  def short: LocalTimeStringConverter = new LocalTimeStringConverter(FormatStyle.SHORT)
}