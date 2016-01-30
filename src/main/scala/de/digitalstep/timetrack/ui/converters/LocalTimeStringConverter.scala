package de.digitalstep.timetrack.ui.converters

import java.time.temporal.{ChronoUnit, Temporal, TemporalUnit}
import java.time.{Duration, LocalTime}
import java.time.format.{DateTimeFormatter, FormatStyle}

import scalafx.util.StringConverter

object LocalTimeStringConverter {
  def short: LocalTimeStringConverter = new LocalTimeStringConverter(FormatStyle.SHORT)
}

class LocalTimeStringConverter private(style: FormatStyle) extends StringConverter[LocalTime] {

  private[this] val formatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedTime(style)

  override def fromString(string: String) = LocalTime.parse(string, formatter)

  override def toString(t: LocalTime): String = t.format(formatter)

}


