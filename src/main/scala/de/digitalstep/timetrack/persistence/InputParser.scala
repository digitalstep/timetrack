package de.digitalstep.timetrack.persistence

import java.time.{LocalDate, LocalTime}

import org.parboiled2.{CharPredicate, Parser, ParserInput, Rule1}

import scala.io.Codec
import scala.reflect.io.File


object InputParser {
  implicit val codec = Codec.UTF8

  private[this] def file2string(file: File): String = io.Source.fromInputStream(file.inputStream()).mkString

  def apply(file: File): InputText = apply(file2string(file))

  def apply(input: ParserInput): InputText = new InputParser(input).all.run().get
}

class InputParser(val input: ParserInput) extends Parser {

  def all = rule {
    ws.* ~ sections ~ ws.* ~ EOI
  }

  def sections = rule {
    section.* ~> InputText
  }

  def section: Rule1[Section] = rule {
    day | comment
  }

  def day: Rule1[Section] = rule {
    isoDate ~ nl.+ ~ tasks ~ zeroOrMore("\n") ~> Day
  }

  def isoDate = rule {
    isoYear ~ "-" ~ isoMonth ~ "-" ~ isoDayOfMonth ~> { (y, m, d) ⇒
      LocalDate.of(y, m, d)
    }
  }

  def isoYear = rule {
    capture("2015") ~> (_.toInt)
  }

  def isoMonth = rule {
    number
  }

  def isoDayOfMonth = rule {
    number
  }

  def tasks = rule {
    zeroOrMore(task)
  }

  def task = rule {
    time ~ rangeSeparator ~ time ~ "   " ~ name ~ emptyLIne ~> Task
  }

  def time = rule {
    zeroOrMore(" ") ~ hours ~ ":" ~ minutes ~> { (h, m) ⇒ LocalTime.of(h, m) }
  }

  def rangeSeparator = rule {
    ws ~ "-" ~ ws
  }

  def hours = rule {
    capture(digits) ~> (_.toInt)
  }

  def minutes = rule {
    capture(digits) ~> (_.toInt)
  }

  def number = rule {
    capture(digits) ~> (_.toInt)
  }

  def digits = rule {
    CharPredicate.Digit ~ CharPredicate.Digit.?
  }

  def name = rule {
    capture((!eol ~ ANY).+)
  }

  def comment: Rule1[Comment] = rule {
    "#" ~ capture((!eol ~ ANY).*) ~ eol ~> Comment
  }

  def ws = rule {
    anyOf(" \n\r\t\f").+
  }

  def emptyLIne = rule {
    nl ~ anyOf(" \n\r\t\f").*
  }

  def eol = rule {
    nl | EOI
  }

  def nl = rule {
    "\n" | "\r\n"
  }

}

