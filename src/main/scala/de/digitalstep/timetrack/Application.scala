package de.digitalstep.timetrack

import java.util.Locale

import com.typesafe.scalalogging.LazyLogging
import de.digitalstep.timetrack.Repository.Delete
import de.digitalstep.timetrack.ui._

import scala.language.implicitConversions
import scalafx.application.JFXApp
import scalafx.collections.ObservableBuffer
import scalafx.collections.ObservableBuffer.{Remove, Add}

object Application extends JFXApp with LazyLogging {
  Locale.setDefault(Locale.GERMANY)

  private[this] val repository = Repository()

  private[this] val workUnits = ObservableBuffer {
    repository.findAll.map(WorkUnitAdapter.apply).toSeq
  }

  workUnits.onChange((buffer, changes) ⇒ for (change ← changes) {
    change match {
      case Add(_, added: Traversable[WorkUnitAdapter]) ⇒ repository.add(added.map(_.get))
      case Remove(_, removed: Traversable[WorkUnitAdapter]) ⇒ repository.delete(removed.map(_.get))
      case x ⇒ logger.debug("Change not handled {}", x);
    }
  })

  stage = ui.PrimaryStage(workUnits, repository.findTasks)
  ui.StageTrayIcon(stage)
}
