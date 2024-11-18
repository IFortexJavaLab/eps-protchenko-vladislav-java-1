package com.ifortex.internship.exception;

import com.ifortex.internship.exception.enums.ErrorCode;
import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {

  private final ErrorCode code;

  private final String message;

  public EntityNotFoundException(ErrorCode code, String message) {
    super(message);
    this.message = message;
    this.code = code;
  }
}
