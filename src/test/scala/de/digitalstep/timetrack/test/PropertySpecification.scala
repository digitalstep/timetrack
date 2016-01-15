package de.digitalstep.timetrack.test

import org.scalatest.prop.PropertyChecks
import org.scalatest.{Matchers, PropSpecLike}

/**
  * Base class for property-based unit tests.
  */
trait PropertySpecification extends PropSpecLike with PropertyChecks with Matchers

