package de.digitalstep.timetrack

import java.util.Locale
import java.util.Locale.GERMANY

import com.typesafe.scalalogging.LazyLogging
import de.digitalstep.timetrack.ui._
import scalafx.application.JFXApp

import scala.language.implicitConversions

object Application extends JFXApp with LazyLogging {
  Locale.setDefault(GERMANY)

  private[this] val actionContext = new DefaultActionContext(Repository())

  stage = ui.PrimaryStage(actionContext)

  ui.StageTrayIcon(stage, actionContext)
}
