package de.digitalstep.timetrack

import java.time.format.{DateTimeFormatter, FormatStyle}
import java.time.{Duration, LocalDateTime}
import java.util.Locale

import de.digitalstep.timetrack.io.FileDatabase

import scala.reflect.io.File
import scala.reflect.io.Path.string2path

/**
 * @author gunnar
 */
object Launcher extends App {
  Locale.setDefault(Locale.GERMANY)

  final val file = File(sys.props("user.home") / ".digitalstep" / "Zeiterfassung.txt")
  type OptionMap = Map[Symbol, Any]

  private[this] def printUsage(options: OptionMap): Unit = println {
    """
      | Usage:
    """.stripMargin
  }

  def printEntries(options: OptionMap): Unit = {
    FileDatabase(file).findAll foreach println
  }

  def printDays(options: OptionMap): Unit = {
    val map = FileDatabase(file).findDays
    map.keys.toList.sortWith(_ isBefore _) foreach { x ⇒
      val day = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).format(x)
      val workUnits = map(x)
      val sum = workUnits.foldLeft(Duration.ofHours(0)) { (d: Duration, w: WorkUnit) ⇒
        d.plus(w.duration)
      }
      println(s"$day   ${TemporalFormat(sum)}")
      workUnits foreach { workUnit ⇒
        val from = TemporalFormat.time(workUnit.from)
        val to = TemporalFormat.time(workUnit.to)
        val duration = TemporalFormat(workUnit.duration)
        println(s"   $from - $to: ${workUnit.description}    $duration")
      }
    }
  }

  def subcommand(x: String): OptionMap ⇒ Unit = x match {
    case "entries" ⇒ printEntries
    case "days" ⇒ printDays
    case _ ⇒ printUsage
  }

  def nextOption(map: OptionMap, list: List[String]): OptionMap = {
    list match {
      case Nil ⇒ map
      case "-month" :: month :: tail ⇒ nextOption(map ++ Map('month → month), tail)
      case x ⇒ throw new IllegalArgumentException(s"$x is not recognized")
    }
  }

  subcommand(args.toList.head)(nextOption(Map(), args.toList.tail))
}

object TemporalFormat {
  def apply(d: Duration): String = s"${d.toMinutes / 60}:${d.toMinutes % 60}"

  def date(d: LocalDateTime): String = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).format(d)

  def time(d: LocalDateTime): String = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).format(d)
}
