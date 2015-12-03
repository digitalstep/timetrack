package de.digitalstep.timetrack

import java.util.Locale

import ui._

import scala.language.implicitConversions
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.collections.ObservableBuffer
import scalafx.collections.ObservableBuffer.Add
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout.BorderPane

object Application extends JFXApp {
  Locale.setDefault(Locale.GERMANY)

  private[this] val repository = Repository()
  repository.onChange(change ⇒ {
    println(change)
    println(repository.findAll.mkString)
  })

  private[this] val workUnits: ObservableBuffer[WorkUnitAdapter] =
    ObservableBuffer(repository.findAll.map(WorkUnitAdapter.apply).toSeq)

  workUnits.onChange((buffer, changes) ⇒ for (change ← changes) {
    change match {
      case Add(_, added: Traversable[WorkUnitAdapter]) ⇒ repository.add(added.map(_.get))
      case x ⇒ println(x)
    }
  })

  lazy val toolbar = new ToolBar {
    content = Seq(
      new Button {
        text = "+"
        onAction = { () ⇒ {
          val dialog = new EditWorkUnitDialog(WorkUnitAdapter())
          dialog.showAndWait() match {
            case Some(_) ⇒ {
              workUnits.add(dialog.getResult)
              workUnits.sort((a, b) ⇒ a.get > b.get)
            }
            case _ ⇒ println("Cancelled")
          }
        }
        }
      }
    )
  }

  lazy val workUnitTable = new WorkUnitTable(workUnits)

  stage = new JFXApp.PrimaryStage {
    title = "timetrack"
    scene = new Scene {
      root = new BorderPane {

        top = toolbar

        center = new TabPane {
          tabs = Seq(
            new Tab {
              text = "Erfassung"
              content = workUnitTable
            }
          )
        }
      }
    }
  }
}
