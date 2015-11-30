package de.digitalstep.timetrack

import java.time.{LocalDate, LocalDateTime, LocalTime}

import scala.language.implicitConversions

object Implicits {

  implicit def tupleToLocalDateTime(tuple: (LocalDate, LocalTime)): LocalDateTime =
    LocalDateTime.of(tuple._1, tuple._2)

}
