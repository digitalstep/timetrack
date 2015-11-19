package de.digitalstep.timetrack.io

trait DataReader {

  def sections: Iterable[Section]

}
