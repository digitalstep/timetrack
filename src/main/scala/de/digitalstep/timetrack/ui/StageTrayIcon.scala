package de.digitalstep.timetrack.ui

import java.awt._
import java.awt.event.{ActionEvent, ActionListener}
import java.lang.Runtime.getRuntime
import javax.imageio.ImageIO
import javax.swing.SwingUtilities

import com.typesafe.scalalogging.LazyLogging
import sun.security.pkcs11.wrapper.CK_C_INITIALIZE_ARGS

import scalafx.application.Platform
import scalafx.stage.Stage

object StageTrayIcon extends Runnables with LazyLogging {

  def apply(stage: Stage): () ⇒ Unit = {
    if (SystemTray.isSupported) setupTrayIcon(stage)
    else () ⇒ sys.exit()
  }

  private[this] def setupTrayIcon(stage: Stage) = {
    Platform.implicitExit = false
    val trayIcon = new StageTrayIcon(stage)
    SwingUtilities.invokeLater(trayIcon.initializer)
    getRuntime.addShutdownHook(new Thread(trayIcon.finalizer))
    trayIcon.finalizer
  }

}

class StageTrayIcon(stage: Stage) extends ImplicitActionListeners with LazyLogging {

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

  private[this] def popupMenu = Build(new PopupMenu)(_ add quitItem)

  private[this] def quitItem = Build(new MenuItem("Quit"))(_ addActionListener finalizer)

  private[this] def toggleStage(): Unit = if (stage.isShowing) stage.hide() else stage.show()

}

object Build {
  def apply[T](t: T)(f: T ⇒ Any): T = {
    f(t)
    t
  }
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