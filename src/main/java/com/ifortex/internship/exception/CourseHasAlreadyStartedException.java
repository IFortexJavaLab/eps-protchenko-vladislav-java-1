package com.ifortex.internship.exception;

import com.ifortex.internship.exception.enums.ErrorCode;
import lombok.Getter;

@Getter
public class CourseHasAlreadyStartedException extends RuntimeException {

  private final ErrorCode code;

  private final String message;

  public CourseHasAlreadyStartedException(ErrorCode code, String message) {
    super(message);
    this.message = message;
    this.code = code;
  }
}
