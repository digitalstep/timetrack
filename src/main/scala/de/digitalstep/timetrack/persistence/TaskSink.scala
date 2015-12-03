package de.digitalstep.timetrack.persistence

import java.time.LocalDate

trait TaskSink {

  def add(date: LocalDate, t: Task): TaskSink

  def save(): Unit

}
