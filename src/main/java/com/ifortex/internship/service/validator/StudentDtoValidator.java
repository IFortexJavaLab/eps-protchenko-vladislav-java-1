package com.ifortex.internship.service.validator;

import com.ifortex.internship.dto.StudentDto;
import com.ifortex.internship.exception.InvalidRequestDataException;
import org.springframework.stereotype.Component;

@Component
public class StudentDtoValidator {

  private final int MIN_NAME_LENGTH = 2;
  private final int MAX_NAME_LENGTH = 255;

  private void validateName(String name) {
    if (name != null
        && !name.isBlank()
        && name.length() >= MIN_NAME_LENGTH
        && name.length() <= MAX_NAME_LENGTH) {
      throw new InvalidRequestDataException("Invalid student name");
    }
  }

  public void validate(StudentDto studentDto) {
    validateName(studentDto.getName());
  }
}
