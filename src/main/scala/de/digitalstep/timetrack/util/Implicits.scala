package de.digitalstep.timetrack.util

import java.time.{LocalDate, LocalDateTime, LocalTime}

import scala.language.implicitConversions

object Implicits {
  implicit def tupleToLocalDateTime(tuple: (LocalDate, LocalTime)): LocalDateTime = {
    val (date, time) = tuple
    LocalDateTime.of(date, time)
  }
}
