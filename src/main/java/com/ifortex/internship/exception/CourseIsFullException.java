package com.ifortex.internship.exception;

public class CourseIsFullException extends RuntimeException {

  public CourseIsFullException(String message) {
    super(message);
  }
}
