package com.ifortex.internship.service.validator;

import com.ifortex.internship.dto.CourseDto;
import com.ifortex.internship.dto.StudentDto;
import com.ifortex.internship.exception.CourseHasAlreadyStartedException;
import com.ifortex.internship.exception.CourseIsFullException;
import com.ifortex.internship.exception.enums.ErrorCode;
import com.ifortex.internship.exception.InvalidRequestDataException;
import com.ifortex.internship.model.Course;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourseDtoValidator {

  private final int MIN_NAME_LENGTH = 2;
  private final int MAX_NAME_LENGTH = 255;
  private final double MIN_PRICE = 0;
  private final int MIN_DURATION = 1;
  private final int MAX_STUDENTS_COUNT = 150;

  public void validateForCreate(CourseDto courseDto) {
    validateName(courseDto.getName());
    validatePrice(courseDto.getPrice());
    validateDuration(courseDto.getDuration());
    validateIsOpenForCreate(courseDto.getIsOpen());
    validateStartDateForCreate(courseDto.getStartDate());
    if (courseDto.getStudents() != null) {
      validateStudents(courseDto.getStudents());
    }
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
      validateIsOpenForUpdate(courseDto.getIsOpen(), course.isOpen(), startDate);
    }
    if (courseDto.getStudents() != null) {
      validateStudents(courseDto.getStudents());
    }
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
          ErrorCode.INVALID_COURSE_REQUEST_DATA, "Invalid course name");
    }
  }

  private void validatePrice(BigDecimal price) {
    boolean isPriceInvalid = price == null || price.doubleValue() < MIN_PRICE;
    if (isPriceInvalid) {
      throw new InvalidRequestDataException(
          ErrorCode.INVALID_COURSE_REQUEST_DATA, "Invalid course price");
    }
  }

  private void validateDuration(int duration) {
    boolean isDurationInvalid = duration < MIN_DURATION;
    if (isDurationInvalid) {
      throw new InvalidRequestDataException(
          ErrorCode.INVALID_COURSE_REQUEST_DATA, "Invalid course duration");
    }
  }

  private void validateStartDateForUpdate(LocalDate newStartDate, LocalDate oldStartDate) {
    if (!oldStartDate.isAfter(LocalDate.now())) {
      throw new CourseHasAlreadyStartedException(
          ErrorCode.COURSE_HAS_ALREADY_STARTED,
          "Course has already started. Can not change start date");
    }
    if (newStartDate.isBefore(LocalDate.now())) {
      throw new InvalidRequestDataException(
          ErrorCode.INVALID_COURSE_REQUEST_DATA, "Invalid course start date");
    }
  }

  private void validateStartDateForCreate(LocalDateTime date) {
    boolean isStartDateInvalid = date == null || date.toLocalDate().isBefore(LocalDate.now());
    if (isStartDateInvalid) {
      throw new InvalidRequestDataException(
          ErrorCode.INVALID_COURSE_REQUEST_DATA, "Invalid course start date");
    }
  }

  private void validateIsOpenForCreate(Boolean isOpen) {
    boolean isStatusNotProvided = isOpen == null;
    if (isStatusNotProvided) {
      throw new InvalidRequestDataException(
          ErrorCode.INVALID_COURSE_REQUEST_DATA, "Invalid course status");
    }
  }

  private void validateIsOpenForUpdate(boolean newIsOpen, boolean oldIsOpen, LocalDate startDate) {
    boolean courseAlreadyStarted = !startDate.isAfter(LocalDate.now());
    boolean isOpeningClosedCourse = !oldIsOpen && newIsOpen;
    boolean invalidStatusChange = courseAlreadyStarted && isOpeningClosedCourse;
    if (invalidStatusChange) {
      throw new CourseHasAlreadyStartedException(
          ErrorCode.COURSE_HAS_ALREADY_STARTED,
          "Course has already started. Can not change status");
    }
  }

  private void validateStudents(List<StudentDto> studentList) {
    if (studentList.size() > MAX_STUDENTS_COUNT) {
      throw new CourseIsFullException(
          ErrorCode.COURSE_IS_FULL, "Course can't have more than 150 students");
    }
  }
}
