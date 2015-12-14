package de.digitalstep.timetrack.persistence

trait SectionSource {

  def days: Iterable[Day]

}
