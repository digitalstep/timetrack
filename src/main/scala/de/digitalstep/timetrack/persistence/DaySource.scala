package de.digitalstep.timetrack.persistence

trait DaySource {

  def days: Iterable[Day]

}
