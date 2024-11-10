package com.ifortex.internship.service.validator;

import com.ifortex.internship.dto.CourseDto;
import com.ifortex.internship.dto.StudentDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourseDtoValidator {

  private final StudentDtoValidator studentDtoValidator;

  private boolean isValidName(String name) {
    return name.trim().length() >= 2 && name.trim().length() <= 255;
  }

  private boolean isValidPrice(BigDecimal price) {
    return price.doubleValue() >= 0;
  }

  private boolean isValidDuration(int duration) {
    return duration >= 0;
  }

  private boolean isValidStartDate(LocalDateTime startDate) {
    return startDate.isAfter(LocalDateTime.now());
  }

  private boolean isValidStudentList(List<StudentDto> studentList) {
    return studentList.stream().allMatch(studentDtoValidator::isValid);
  }

  public boolean isValidForCreate(CourseDto courseDto) {
    if (courseDto.students() == null) return false;
    return isValidName(courseDto.name())
        && isValidPrice(courseDto.price())
        && isValidDuration(courseDto.duration())
        && isValidStartDate(courseDto.startDate())
        && isValidStudentList(courseDto.students());
  }

  public boolean isValidForUpdate(CourseDto courseDto) {
    boolean isValid = false;
    if (courseDto.name() != null) isValid = isValidName(courseDto.name());
    if (courseDto.price() != null) isValid = isValid && isValidPrice(courseDto.price());
    if (courseDto.duration() != null) isValid = isValid && isValidDuration(courseDto.duration());
    if (courseDto.startDate() != null) isValid = isValid && isValidStartDate(courseDto.startDate());
    if (courseDto.students() != null) isValid = isValid && isValidStudentList(courseDto.students());
    return isValid;
  }
}
