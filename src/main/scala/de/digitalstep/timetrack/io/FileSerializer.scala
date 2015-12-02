package de.digitalstep.timetrack.io

import scala.reflect.io.Path

object FileSerializer {
  def apply(path: Path): FileSerializer = new FileSerializer(path)
}

class FileSerializer(path: Path) extends Serializer {
  protected def outputStream() = path.toFile.outputStream()
}
