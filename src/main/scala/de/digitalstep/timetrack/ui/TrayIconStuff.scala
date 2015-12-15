package de.digitalstep.timetrack.ui

import java.awt.event.{ActionEvent, ActionListener}
import java.awt._
import com.typesafe.scalalogging.LazyLogging

import scalafx.Includes._
import scalafx.stage.Stage
import javax.imageio.ImageIO
import javax.swing.SwingUtilities

import scalafx.application.Platform

object TrayIconStuff extends LazyLogging {

  implicit def runnable(f: => Unit): Runnable = new Runnable() {
    def run() = f
  }

  def apply(stage: Stage) = {
    if (SystemTray.isSupported) {
      Platform.implicitExit = false
      SwingUtilities.invokeLater(addAppToTray(createTrayIcon(stage)))
    } else {
      logger.info("System tray not supported on this platform")
    }
  }

  private[this] def addAppToTray(trayIcon: TrayIcon): Unit = {
    Toolkit.getDefaultToolkit // make sure it's initialized

    SystemTray.getSystemTray.add(trayIcon)
    Runtime.getRuntime.addShutdownHook(new Thread {
      override def run(): Unit = {
        SystemTray.getSystemTray.remove(trayIcon)
        logger.debug("Removed system tray icon")
      }
    })
  }


  def createTrayIcon(stage: Stage): TrayIcon = new StageTrayIcon(stage).delegate

}

class StageTrayIcon(stage: Stage) {
  private[this] implicit def actionListener(f: () ⇒ Unit): ActionListener = new ActionListener {
    override def actionPerformed(e: ActionEvent): Unit = f()
  }

  val delegate = new TrayIcon(ImageIO.read(getClass.getResource("/tray.png")))

  delegate.addActionListener { () ⇒
    Platform.runLater(toggleStage())
  }

  delegate.setPopupMenu(popupMenu)

  private[this] def popupMenu: PopupMenu = {
    val popup = new PopupMenu()
    popup.add(quitItem)
    popup
  }

  def quitItem: MenuItem = {
    val item = new MenuItem("Quit")
    item.addActionListener { () ⇒ {
      Platform.exit()
      SystemTray.getSystemTray.remove(delegate)
    }
    }
    item
  }

  private[this] def toggleStage(): Unit = {
    if (stage.isShowing) {
      stage.hide()
    } else {
      stage.show()
      stage.toFront()
    }
  }

}
