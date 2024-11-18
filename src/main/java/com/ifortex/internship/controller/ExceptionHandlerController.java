package com.ifortex.internship.controller;

import com.ifortex.internship.dto.ErrorResponseDto;
import com.ifortex.internship.exception.CourseHasAlreadyStartedException;
import com.ifortex.internship.exception.CourseIsFullException;
import com.ifortex.internship.exception.EntityNotFoundException;
import com.ifortex.internship.exception.InvalidRequestDataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerController {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorResponseDto> handleEntityNotFoundException(EntityNotFoundException e) {
    ErrorResponseDto dto = new ErrorResponseDto(e.getMessage(), e.getCode().getValue());
    return new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(InvalidRequestDataException.class)
  public ResponseEntity<ErrorResponseDto> handleInvalidRequestDataException(
      InvalidRequestDataException e) {
    ErrorResponseDto dto = new ErrorResponseDto(e.getMessage(), e.getCode().getValue());
    return new ResponseEntity<>(dto, HttpStatus.NOT_ACCEPTABLE);
  }

  @ExceptionHandler(CourseHasAlreadyStartedException.class)
  public ResponseEntity<ErrorResponseDto> handleCourseHasAlreadyStartedException(
      CourseHasAlreadyStartedException e) {
    ErrorResponseDto dto = new ErrorResponseDto(e.getMessage(), e.getCode().getValue());
    return new ResponseEntity<>(dto, HttpStatus.NOT_ACCEPTABLE);
  }

  @ExceptionHandler(CourseIsFullException.class)
  public ResponseEntity<ErrorResponseDto> handleCourseIsFullException(CourseIsFullException e) {
    ErrorResponseDto dto = new ErrorResponseDto(e.getMessage(), e.getCode().getValue());
    return new ResponseEntity<>(dto, HttpStatus.NOT_ACCEPTABLE);
  }
}
