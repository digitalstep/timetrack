package de.digitalstep.timetrack.persistence

import java.io.{OutputStream, PrintWriter}

import com.typesafe.scalalogging.LazyLogging

import scala.reflect.io.Path

object Serializer {

  def apply(path: Path): Serializer = new Serializer {
    protected def outputStream() = path.toFile.outputStream()
  }

  def apply(out: OutputStream) = new Serializer {
    protected def outputStream(): OutputStream = out

    override protected def close(out: AutoCloseable): Unit = {}
  }

}

trait Serializer extends LazyLogging {

  protected def outputStream(): OutputStream

  protected def close(out: AutoCloseable): Unit = out.close()

  protected[persistence] def serialize(e: InputText): Unit = {
    val writer = new PrintWriter(outputStream())
    try {
      val serialized = toString(e)
      logger.debug(s"$e\n$serialized")
      writer.println(serialized)
    } finally {
      close(writer)
    }
  }

  private[this] def toString(e: Element): String = e match {
    case InputText(sections) ⇒ sections.map(toString).mkString("\n")
    case Day(date, tasks) ⇒ s"$date\n${tasks.map(toString).mkString}"
    case Task(from, to, name) ⇒ s"$from - $to   $name\n"
    case WhiteSpace(x) ⇒ x
  }

}
