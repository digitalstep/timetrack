package de.digitalstep.timetrack.io

import java.time.{LocalTime, LocalDate}

sealed trait Element

sealed trait Section extends Element

case class InputText(sections: Iterable[Section]) extends Element

case class Day(date: LocalDate, tasks: Seq[Task]) extends Section

case class Comment(text: String) extends Section

case class Task(from: LocalTime, to: LocalTime, name: String) extends Element

case class WhiteSpace(text: String) extends Element
