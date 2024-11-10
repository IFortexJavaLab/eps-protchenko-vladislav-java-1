package com.ifortex.internship.service.validator;

import com.ifortex.internship.dto.StudentDto;
import org.springframework.stereotype.Component;

@Component
public class StudentDtoValidator {
  
  private boolean isValidName(String name) {
    return name != null
            && !name.isBlank()
            && name.length() >= 2
            && name.length() <= 255;
  }

  public boolean isValid(StudentDto studentDto) {
    return isValidName(studentDto.name());
  }
}
