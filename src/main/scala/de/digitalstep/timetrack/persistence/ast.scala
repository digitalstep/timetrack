package de.digitalstep.timetrack.persistence

import java.time.{LocalTime, LocalDate}

sealed trait Element {
  val hasChildren: Boolean
}

case class InputText(days: Iterable[Day]) extends Element {
  val hasChildren = true
}

case class Day(date: LocalDate, tasks: Seq[Task]) extends Element {
  val hasChildren = true
}

case class Task(from: LocalTime, to: LocalTime, name: String) extends Element {
  val hasChildren = false
}

case class WhiteSpace(text: String) extends Element {
  val hasChildren = false
}
