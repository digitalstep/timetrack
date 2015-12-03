package de.digitalstep.timetrack.persistence

import java.io.{PrintWriter, OutputStream}

trait Serializer {

  protected def outputStream(): OutputStream

  protected def close(out: AutoCloseable): Unit = out.close()

  protected[persistence] def serialize(e: Element): Unit = {
    val writer = new PrintWriter(outputStream())
    try {
      writer.println(toString(e))
    } finally {
      close(writer)
    }
  }

  private[this] def toString(e: Element): String = e match {
    case InputText(sections) ⇒ sections.map(toString).mkString
    case Comment(text) ⇒ s"# $text"
    case Day(date, tasks) ⇒ s"$date\n${tasks.map(toString).mkString}"
    case Task(from, to, name) ⇒ s"$from - $to   $name\n"
    case WhiteSpace(x) ⇒ x
  }

}
