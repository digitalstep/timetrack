package de.digitalstep.timetrack.io

trait SectionSource {

  def sections: Iterable[Section]

  def days: Iterable[Day] = sections filter (_.isInstanceOf[Day]) map (_.asInstanceOf[Day])

}
