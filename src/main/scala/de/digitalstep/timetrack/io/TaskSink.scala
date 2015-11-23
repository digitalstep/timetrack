package de.digitalstep.timetrack.io

trait TaskSink {

  def take(t: Task): Unit

}
