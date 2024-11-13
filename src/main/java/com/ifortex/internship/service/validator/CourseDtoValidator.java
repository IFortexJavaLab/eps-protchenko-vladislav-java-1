package com.ifortex.internship.service.validator;

import com.ifortex.internship.dto.CourseDto;
import com.ifortex.internship.dto.StudentDto;
import com.ifortex.internship.exception.CourseHasAlreadyStartedException;
import com.ifortex.internship.exception.CourseIsFullException;
import com.ifortex.internship.exception.InvalidRequestDataException;
import com.ifortex.internship.model.Course;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourseDtoValidator {

  private final int MIN_NAME_LENGTH = 2;
  private final int MAX_NAME_LENGTH = 255;
  private final double MIN_PRICE = 0;
  private final int MIN_DURATION = 0;
  private final int MAX_STUDENTS_COUNT = 150;

  private final StudentDtoValidator studentDtoValidator;

  private void validateName(String name) {
    if (name.trim().length() < MIN_NAME_LENGTH || name.trim().length() > MAX_NAME_LENGTH) {
      throw new InvalidRequestDataException("Invalid course name");
    }
  }

  private void validatePrice(BigDecimal price) {
    if (price.doubleValue() < MIN_PRICE) {
      throw new InvalidRequestDataException("Invalid course price");
    }
  }

  private void validateDuration(int duration) {
    if (duration < MIN_DURATION) {
      throw new InvalidRequestDataException("Invalid course duration");
    }
  }

  private void validateStartDateForUpdate(LocalDate newStartDate, LocalDate oldStartDate) {
    if (!oldStartDate.isAfter(LocalDate.now())) {
      throw new CourseHasAlreadyStartedException("Course has already started. Can not change start date");
    }
    if (newStartDate.isBefore(LocalDate.now())) {
      throw new InvalidRequestDataException("Invalid course start date");
    }
  }

  private void validateStartDateForCreate(LocalDate date) {
    if (date.isBefore(LocalDate.now())) {
      throw new InvalidRequestDataException("Invalid course start date");
    }
  }

  private void validateIsOpen(boolean newIsOpen, boolean oldIsOpen, LocalDate startDate) {
    if (!startDate.isAfter(LocalDate.now()) && !oldIsOpen && newIsOpen) {
      throw new CourseHasAlreadyStartedException("Course has already started. Can not change status");
    }
  }

  private void validateStudents(List<StudentDto> studentList) {
    if (studentList.size() > MAX_STUDENTS_COUNT) {
      throw new CourseIsFullException("Course can't have more than 150 students");
    }
    studentList.forEach(studentDtoValidator::validate);
  }

  public void validateForCreate(CourseDto courseDto) {
    if (courseDto.getStudents() == null) {
      throw new InvalidRequestDataException("Invalid course students");
    }
    if (courseDto.getStudents().size() > MAX_STUDENTS_COUNT) {
      throw new CourseIsFullException("Course can not have more than 150 students");
    }
    validateName(courseDto.getName());
    validatePrice(courseDto.getPrice());
    validateDuration(courseDto.getDuration());
    validateStartDateForCreate(courseDto.getStartDate().toLocalDate());
    validateStudents(courseDto.getStudents());
  }

  public void validateForUpdate(CourseDto courseDto, Course course) {
    if (courseDto.getName() != null) {
      validateName(courseDto.getName());
    }
    if (courseDto.getPrice() != null) {
      validatePrice(courseDto.getPrice());
    }
    if (courseDto.getDuration() != null) {
      validateDuration(courseDto.getDuration());
    }
    if (courseDto.getStartDate() != null) {
      validateStartDateForUpdate(
          courseDto.getStartDate().toLocalDate(), course.getStartDate().toLocalDate());
    }
    if (courseDto.getIsOpen() != null) {
      LocalDate startDate =
              courseDto.getStartDate() != null
                      ? courseDto.getStartDate().toLocalDate()
                      : course.getStartDate().toLocalDate();
      validateIsOpen(courseDto.getIsOpen(), course.isOpen(), startDate);
    }
    if (courseDto.getStudents() != null) {
      validateStudents(courseDto.getStudents());
    }
  }
}
