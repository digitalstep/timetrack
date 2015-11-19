package de.digitalstep.timetrack

import java.time.format.{FormatStyle, DateTimeFormatter}
import java.time.{LocalDateTime, LocalTime, LocalDate}
import scala.language.implicitConversions

object Implicits {

  implicit def tupleToLocalDateTime(tuple: (LocalDate, LocalTime)): LocalDateTime =
    LocalDateTime.of(tuple._1, tuple._2)

}
