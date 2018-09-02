package de.digitalstep.timetrack

import java.time.{LocalDate, LocalTime}

package object persistence {

  implicit def time(hours: Int, minutes: Int): LocalTime = LocalTime.of(hours, minutes)

  implicit def date(year: Int, month: Int, day: Int): LocalDate = LocalDate.of(year, month, day)

  implicit val LocalDateOrdering: Ordering[LocalDate] = (a, b) ⇒ b compareTo a

}
