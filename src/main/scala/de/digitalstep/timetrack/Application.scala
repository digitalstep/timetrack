package de.digitalstep.timetrack

import java.util.Locale

import de.digitalstep.timetrack.ui._

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

  def lt(a: WorkUnitAdapter, b: WorkUnitAdapter) = a.get > b.get

  private[this] val workUnits: ObservableBuffer[WorkUnitAdapter] =
    ObservableBuffer(Repository().findAll.map(WorkUnitAdapter.apply).toSeq)

  workUnits.onChange((buffer, changes) ⇒ for (change ← changes) {
    change match {
      case Add(_, added: Traversable[WorkUnitAdapter]) ⇒ Repository().add(added.map(_.get))
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
              workUnits.sort(lt)
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
