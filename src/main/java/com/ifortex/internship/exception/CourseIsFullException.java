package com.ifortex.internship.exception;

import lombok.Getter;

@Getter
public class CourseIsFullException extends RuntimeException {

  private final ErrorCode code;

  private final String message;

  public CourseIsFullException(ErrorCode code, String message) {
    super(message);
    this.message = message;
    this.code = code;
  }
}
