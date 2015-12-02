package de.digitalstep.timetrack.io

trait SectionSource {

  val sections: Iterable[Section]

  def days: Iterable[Day] = sections filter (_.isInstanceOf[Day]) map (_.asInstanceOf[Day])

}
