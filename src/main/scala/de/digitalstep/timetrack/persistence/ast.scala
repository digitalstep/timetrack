package de.digitalstep.timetrack.persistence

import java.time.{LocalTime, LocalDate}

sealed trait Element {
  val hasChildren: Boolean
}

sealed trait Section extends Element

case class InputText(sections: Iterable[Section]) extends Element {
  val hasChildren = true
}

case class Day(date: LocalDate, tasks: Seq[Task]) extends Section {
  val hasChildren = true
}

case class Comment(text: String) extends Section {
  val hasChildren = false
}

case class Task(from: LocalTime, to: LocalTime, name: String) extends Element {
  val hasChildren = false
}

case class WhiteSpace(text: String) extends Element {
  val hasChildren = false
}
