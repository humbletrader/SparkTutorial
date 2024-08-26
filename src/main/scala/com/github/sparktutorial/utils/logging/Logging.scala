package com.github.sparktutorial.utils.logging

import org.slf4j.{Logger, LoggerFactory}

/**
 * quick and dirty scala wrapper over slf4j
 */
trait Logging { self =>

  @transient
  lazy val log: Logger = LoggerFactory.getLogger(self.getClass)

}
