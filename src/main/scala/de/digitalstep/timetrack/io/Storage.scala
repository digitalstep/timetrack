package de.digitalstep.timetrack.io

object Storage {

  def apply(): Storage = TextStorage()

}

trait Storage extends SectionSource with TaskSink
