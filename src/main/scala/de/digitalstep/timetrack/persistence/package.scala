package de.digitalstep.timetrack

import java.time.{LocalDate, LocalTime}

package object persistence {

  def time(hours: Int, minutes: Int) = LocalTime.of(hours, minutes)

  def date(year: Int, month: Int, day: Int) = LocalDate.of(year, month, day)

}
