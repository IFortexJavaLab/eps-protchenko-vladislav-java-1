package com.ifortex.internship.exception;

public class CourseHasAlreadyStartedException extends RuntimeException {
  public CourseHasAlreadyStartedException(String message) {
    super(message);
  }
}
