package de.digitalstep.timetrack.ui

import javafx.beans.{binding ⇒ jfxbb}

import scalafx.beans.value._
import scalafx.beans.{binding ⇒ sfxbb}
import scalafx.util.StringConverter


object StringBinding {
  implicit def sfxStringBinding2jfx(sb: sfxbb.StringBinding): jfxbb.StringBinding = if (sb != null) sb.delegate else null

  def apply[T](observable: ObservableValue[T, T], converter: StringConverter[T]): ObservableValue[String, String] =
    new sfxbb.StringBinding(
      new jfxbb.StringBinding {
        override def computeValue(): String = converter.toString(observable.value)
      })

  def apply(fn: () ⇒ String): sfxbb.StringBinding = new sfxbb.StringBinding(new jfxbb.StringBinding {
    override def computeValue(): String = fn()
  })
}
