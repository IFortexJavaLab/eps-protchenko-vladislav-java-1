package com.ifortex.internship.service.validator;

import com.ifortex.internship.dto.StudentDto;
import com.ifortex.internship.exception.ErrorCode;
import com.ifortex.internship.exception.InvalidRequestDataException;
import org.springframework.stereotype.Component;

@Component
public class StudentDtoValidator {

  private final int MIN_NAME_LENGTH = 2;
  private final int MAX_NAME_LENGTH = 255;

  public void validate(StudentDto studentDto) {
    validateName(studentDto.getName());
  }

  private void validateName(String name) {
    boolean isNameProvided = name != null && !name.isBlank();
    boolean isNameLengthValid =
        name != null
            && name.trim().length() >= MIN_NAME_LENGTH
            && name.trim().length() <= MAX_NAME_LENGTH;
    boolean isNameInvalid = !isNameProvided || !isNameLengthValid;
    if (isNameInvalid) {
      throw new InvalidRequestDataException(
          ErrorCode.INVALID_STUDENT_REQUEST_DATA, "Invalid student name");
    }
  }
}
