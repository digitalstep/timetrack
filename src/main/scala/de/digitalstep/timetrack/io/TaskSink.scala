package de.digitalstep.timetrack.io

import java.time.LocalDate

trait TaskSink {

  def add(date: LocalDate, t: Task): TaskSink

}
