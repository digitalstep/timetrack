package de.digitalstep.timetrack

import java.util.Locale

import com.typesafe.scalalogging.LazyLogging
import de.digitalstep.timetrack.ui._

import scala.language.implicitConversions
import scalafx.application.JFXApp

object Application extends JFXApp with LazyLogging {
  Locale.setDefault(Locale.GERMANY)

  private[this] val actionContext = new ActionContext(Repository())

  stage = ui.PrimaryStage(actionContext)

  ui.StageTrayIcon(stage, actionContext)
}
