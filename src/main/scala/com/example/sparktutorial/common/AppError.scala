package com.example.sparktutorial.common



sealed trait AppError {
  def message: String
}

case class ValidationError(message: String) extends AppError
case class DataProcessingError(message: String, cause: Throwable) extends AppError
case class ConfigurationError(message: String) extends AppError
