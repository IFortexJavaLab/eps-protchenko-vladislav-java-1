package com.ifortex.internship.service.impl;

import com.ifortex.internship.dto.CourseDto;
import com.ifortex.internship.dto.StudentDto;
import com.ifortex.internship.exception.EntityNotFoundException;
import com.ifortex.internship.mapper.CourseMapper;
import com.ifortex.internship.mapper.StudentMapper;
import com.ifortex.internship.model.Course;
import com.ifortex.internship.model.enums.CourseField;
import com.ifortex.internship.repository.CourseRepository;
import com.ifortex.internship.service.CourseService;
import com.ifortex.internship.service.validator.CourseDtoValidator;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

  private final CourseRepository courseRepository;
  private final CourseMapper courseMapper;
  private final StudentMapper studentMapper;
  private final CourseDtoValidator courseDtoValidator;

  public CourseDto getCourse(long id) {
    return courseMapper.toDto(
        courseRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(String.format("Course with id %s not found", id))));
  }

  public List<CourseDto> getAllCourses() {
    return courseMapper.toDto(courseRepository.findAll());
  }

  @Transactional
  public CourseDto createCourse(CourseDto courseDto) {
    courseDtoValidator.validateForCreate(courseDto);
    Course course = courseMapper.toEntity(courseDto);
    course.setLastUpdateDate(LocalDateTime.now());
    return courseMapper.toDto(courseRepository.create(course));
  }

  @Transactional
  public CourseDto updateCourse(CourseDto courseDto) {
    Course oldCourseEntity =
        courseRepository
            .findById(courseDto.getId())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        String.format("Course with id %s not found", courseDto.getId())));
    courseDto.setLastUpdateDate(LocalDateTime.now());
    courseDtoValidator.validateForUpdate(courseDto, oldCourseEntity);
    courseRepository.update(courseDto.getId(), getFieldsForUpdate(courseDto));

    if (courseDto.getStudents() != null) {
      courseRepository.updateCourseStudents(
          oldCourseEntity, courseDto.getStudents().stream().map(StudentDto::getId).toList());
    }

    mapNewDtoFields(oldCourseEntity, courseDto);
    return courseDto;
  }

  @Transactional
  public void deleteCourse(long id) {
    if (courseRepository.findById(id).isEmpty()) {
      throw new EntityNotFoundException(String.format("Course with id %s not found", id));
    }
    courseRepository.delete(id);
  }

  private void mapNewDtoFields(Course oldCourseEntity, CourseDto courseDto) {
    if (courseDto.getName() == null) {
      courseDto.setName(oldCourseEntity.getName());
    }
    if (courseDto.getDescription() == null) {
      courseDto.setDescription(oldCourseEntity.getDescription());
    }
    if (courseDto.getPrice() == null) {
      courseDto.setPrice(oldCourseEntity.getPrice());
    }
    if (courseDto.getStartDate() == null) {
      courseDto.setStartDate(oldCourseEntity.getStartDate());
    }
    if (courseDto.getDuration() == null) {
      courseDto.setDuration(oldCourseEntity.getDuration());
    }
    if (courseDto.getIsOpen() == null) {
      courseDto.setIsOpen(oldCourseEntity.isOpen());
    }
    if (courseDto.getStudents() == null) {
      courseDto.setStudents(studentMapper.toDto(oldCourseEntity.getStudents()));
    }
  }

  private Map<CourseField, Object> getFieldsForUpdate(CourseDto courseDto) {
    Map<CourseField, Object> fields = new HashMap<>();
    if (courseDto.getName() != null) {
      fields.put(CourseField.NAME, courseDto.getName());
    }
    if (courseDto.getDescription() != null) {
      fields.put(CourseField.DESCRIPTION, courseDto.getDescription());
    }
    if (courseDto.getPrice() != null) {
      fields.put(CourseField.PRICE, courseDto.getPrice());
    }
    if (courseDto.getDuration() != null) {
      fields.put(CourseField.DURATION, courseDto.getDuration());
    }
    if (courseDto.getStartDate() != null) {
      fields.put(CourseField.START_DATE, courseDto.getStartDate());
    }
    if (courseDto.getIsOpen() != null) {
      fields.put(CourseField.IS_OPEN, courseDto.getIsOpen());
    }
    fields.put(CourseField.LAST_UPDATE_DATE, courseDto.getLastUpdateDate());

    return fields;
  }
}
