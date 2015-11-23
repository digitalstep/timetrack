package de.digitalstep.timetrack.test

import org.scalatest.{Matchers, PropSpec}
import org.scalatest.prop.PropertyChecks

/**
  * Base class for property-based unit tests.
  */
abstract class PropertySpecification
  extends PropSpec with PropertyChecks
  with Matchers

