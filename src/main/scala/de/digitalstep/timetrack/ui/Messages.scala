package de.digitalstep.timetrack.ui

import java.util.ResourceBundle.getBundle

import scala.language.implicitConversions

object Message extends Messages {
  def apply(symbol: Symbol): String = symbol2String(symbol)
}

trait Messages {
  protected final val Bundle = getBundle(classOf[Messages].getName, new UTF8Control)

  protected implicit def symbol2String(symbol: Symbol): String = Bundle.getString(symbol.name)
}
