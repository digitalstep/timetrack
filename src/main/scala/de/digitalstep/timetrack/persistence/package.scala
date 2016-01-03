package de.digitalstep.timetrack

import java.time.{LocalDate, LocalTime}

package object persistence {

  implicit def time(hours: Int, minutes: Int) = LocalTime.of(hours, minutes)

  implicit def date(year: Int, month: Int, day: Int) = LocalDate.of(year, month, day)

  implicit val LocalDateOrdering = new Ordering[LocalDate] {
    override def compare(x: LocalDate, y: LocalDate) = x compareTo y
  }

}
