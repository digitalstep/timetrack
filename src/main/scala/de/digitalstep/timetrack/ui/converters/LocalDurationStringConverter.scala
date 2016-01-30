package de.digitalstep.timetrack.ui.converters

import java.time.Duration
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit._

import scalafx.util.StringConverter

object LocalDurationStringConverter {
  def short: LocalDurationStringConverter = new LocalDurationStringConverter(FormatStyle.SHORT)
}

class LocalDurationStringConverter private(style: FormatStyle) extends StringConverter[Duration] {

  override def fromString(string: String) = {
    val Array(hours, minutes) = string.split(":").map(_.toInt)
    Duration.ofHours(hours).plus(minutes, MINUTES)
  }

  override def toString(t: Duration): String = "%02d:%02d".format(
    t.toHours,
    t.minus(t.toHours, HOURS).toMinutes
  )

}
