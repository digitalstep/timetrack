package de.digitalstep.timetrack.persistence

object Storage {

  def apply(): Storage = TextStorage()

}

trait Storage extends DaySource with TaskSink
