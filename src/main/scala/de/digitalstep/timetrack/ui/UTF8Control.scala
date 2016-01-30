package de.digitalstep.timetrack.ui

import java.io.{InputStreamReader, InputStream}
import java.util.{PropertyResourceBundle, Locale, ResourceBundle}

class UTF8Control extends ResourceBundle.Control {

  override def newBundle(baseName: String,
                         locale: Locale,
                         format: String,
                         loader: ClassLoader,
                         reload: Boolean): ResourceBundle =
    openStream(loader, reload, resourceName(baseName, locale)).
      map(createResourceBundle).
      orNull

  private[this] def resourceName(baseName: String, locale: Locale): String =
    toResourceName(toBundleName(baseName, locale), "properties")


  private[this] def createResourceBundle: InputStream ⇒ PropertyResourceBundle = { in ⇒
    try new PropertyResourceBundle(new InputStreamReader(in, "UTF-8"))
    finally in.close()
  }

  private[this] def openStream(loader: ClassLoader, reload: Boolean, resourceName: String): Option[InputStream] =
    if (reload)
      Option(loader.getResource(resourceName)).
        map(_.openConnection()).
        map(_.getInputStream)
    else
      Option(loader.getResourceAsStream(resourceName))
}

