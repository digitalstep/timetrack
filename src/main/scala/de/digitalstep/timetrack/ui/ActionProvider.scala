package de.digitalstep.timetrack.ui

import javafx.event.{ActionEvent, EventHandler}

import de.digitalstep.timetrack.Repository

import scalafx.collections.{ObservableBuffer, ObservableSet}
import scalafx.Includes._


class ActionProvider(repository: Repository) {

  private[this] val workUnits = ObservableBuffer {
    repository.findAll.map(WorkUnitAdapter.apply).toSeq
  }
  private[this] val taskSuggestions: ObservableSet[String] = ObservableSet {
    repository.findAllTasks.toSeq
  }

  private[this] def suggest(prefix: String): Iterable[String] = taskSuggestions filter (_ startsWith prefix)

  lazy val createWorkUnit: EventHandler[ActionEvent] = () ⇒ EditWorkUnitDialog.create(suggest, workUnits)

  def updateWorkUnit(current: WorkUnitAdapter): EventHandler[ActionEvent] = () ⇒
    EditWorkUnitDialog.update(current, suggest, workUnits)

  def deleteWorkUnit(firstSelected: WorkUnitAdapter): EventHandler[ActionEvent] = () ⇒ workUnits -= firstSelected

}
