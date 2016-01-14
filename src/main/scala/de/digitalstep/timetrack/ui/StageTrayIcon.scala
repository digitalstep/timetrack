package de.digitalstep.timetrack.ui

import java.awt._
import java.awt.event.{ActionEvent, ActionListener}
import java.lang.Runtime.getRuntime
import javax.imageio.ImageIO
import javax.swing.SwingUtilities

import com.typesafe.scalalogging.LazyLogging

import scala.language.implicitConversions
import scalafx.application.Platform
import scalafx.stage.Stage

object StageTrayIcon extends Runnables with LazyLogging {

  type Finalizer = () ⇒ Unit

  def apply(stage: Stage, actionContext: ActionContext): Finalizer = {
    if (SystemTray.isSupported) setupTrayIcon(stage, actionContext)
    else () ⇒ sys.exit()
  }

  private[this] def setupTrayIcon(stage: Stage, actionContext: ActionContext) = {
    Platform.implicitExit = false
    val trayIcon = new StageTrayIcon(stage, actionContext)
    SwingUtilities.invokeLater(trayIcon.initializer)
    getRuntime.addShutdownHook(new Thread(trayIcon.finalizer))
    trayIcon.finalizer
  }

}

class StageTrayIcon(stage: Stage, actionContext: ActionContext) extends ImplicitActionListeners with LazyLogging {

  import Platform.runLater

  private[this] val delegate: TrayIcon = Build(new TrayIcon(ImageIO.read(getClass.getResource("/tray.png")))) {
    trayIcon ⇒ import trayIcon._
      addActionListener(() ⇒ runLater(toggleStage()))
      setPopupMenu(popupMenu)
  }

  private lazy val initializer = () ⇒ {
    Toolkit.getDefaultToolkit // make sure it's initialized
    SystemTray.getSystemTray.add(delegate)
    logger.debug("Added system tray icon")
  }

  private lazy val finalizer = () ⇒ {
    Platform.exit()
    SystemTray.getSystemTray.remove(delegate)
    logger.debug("Removed system tray icon")
  }

  private lazy val newEntry = () ⇒ {
    runLater(EditWorkUnitDialog.create(actionContext))
  }

  private[this] def popupMenu = Build(new PopupMenu) { m ⇒
    m add Build(new MenuItem("Quit"))(_ addActionListener finalizer)
    m add Build(new MenuItem("New Entry"))(_ addActionListener newEntry)
  }

  private[this] def toggleStage(): Unit = if (stage.isShowing) stage.hide() else stage.show()

}

trait Runnables {
  protected implicit def runnable(f: () ⇒ Unit): Runnable = new Runnable {
    def run() = f()
  }
}

trait ImplicitActionListeners {
  protected implicit def actionListener(f: () ⇒ Unit): ActionListener = new ActionListener {
    override def actionPerformed(e: ActionEvent): Unit = f()
  }
}