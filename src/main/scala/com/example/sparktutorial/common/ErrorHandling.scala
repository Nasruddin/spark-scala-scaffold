package com.example.sparktutorial.common

import org.slf4j.LoggerFactory


object ErrorHandling {
  private val logger = LoggerFactory.getLogger(this.getClass)

  def withErrorHandling[T](operation: => T)(errorMessage: String): Either[AppError, T] = {
    try {
      Right(operation)
    } catch {
      case ex: Exception =>
        logger.error(s"$errorMessage: ${ex.getMessage}", ex)
        Left(DataProcessingError(errorMessage, ex))
    }
  }
}
