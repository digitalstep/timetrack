package de.digitalstep.timetrack.util

import java.time.{LocalDate, LocalDateTime, LocalTime}
import java.util.function.Consumer

import scala.language.implicitConversions

object Implicits {

  implicit def tupleToLocalDateTime(tuple: (LocalDate, LocalTime)): LocalDateTime =
    LocalDateTime.of(tuple._1, tuple._2)

  implicit def lambdaToConsumer[T](lambda: T ⇒ Unit): Consumer[T] =
    new Consumer[T] {
      override def accept(t: T): Unit = lambda(t)
    }

}
