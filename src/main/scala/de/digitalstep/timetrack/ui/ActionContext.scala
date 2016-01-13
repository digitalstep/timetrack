package de.digitalstep.timetrack.ui

import com.typesafe.scalalogging.LazyLogging
import de.digitalstep.timetrack.Repository

import scalafx.collections.ObservableBuffer.{Add, Remove}
import scalafx.collections.{ObservableBuffer, ObservableSet}


class ActionContext(repository: Repository) extends LazyLogging {

  val workUnits = {
    val result = ObservableBuffer {
      repository.findAll.map(WorkUnitAdapter.apply).toSeq
    }
    result.onChange((buffer, changes) ⇒ for (change ← changes) {
      change match {
        case Add(_, added: Traversable[WorkUnitAdapter]) ⇒ repository.add(added.map(_.get))
        case Remove(_, removed: Traversable[WorkUnitAdapter]) ⇒ repository.delete(removed.map(_.get))
        case x ⇒ logger.debug("Change not handled {}", x);
      }
    })
    result
  }

  def suggest(prefix: String): Iterable[String] = taskSuggestions.toSet filter (_ startsWith prefix)

  private[this] val taskSuggestions: ObservableSet[String] = ObservableSet {
    repository.findAllTasks.toSeq
  }

}
