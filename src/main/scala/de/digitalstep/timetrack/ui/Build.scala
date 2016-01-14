package de.digitalstep.timetrack.ui

object Build {
  def apply[T](t: T)(f: T ⇒ Any): T = {
    f(t)
    t
  }
}
