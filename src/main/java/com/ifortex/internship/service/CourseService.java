package com.ifortex.internship.service;

import com.ifortex.internship.dto.CourseDto;
import com.ifortex.internship.dto.StudentDto;
import com.ifortex.internship.exception.CourseIsFullException;
import com.ifortex.internship.exception.EntityNotFoundException;
import com.ifortex.internship.exception.InvalidRequestDataException;
import com.ifortex.internship.mapper.CourseMapper;
import com.ifortex.internship.model.Course;
import com.ifortex.internship.repository.CourseRepository;
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
public class CourseService {

  private final int MIN_STUDENTS = 30;
  private final int MAX_STUDENTS = 150;

  private final CourseRepository courseRepository;

  private final CourseMapper courseMapper;

  private final CourseDtoValidator courseDtoValidator;

  private Map<String, Object> getFieldsForUpdate(CourseDto courseDto) {
    Map<String, Object> fields = new HashMap<>();
    if (courseDto.name() != null) fields.put("name", courseDto.name());
    if (courseDto.description() != null) fields.put("description", courseDto.description());
    if (courseDto.price() != null) fields.put("price", courseDto.price());
    if (courseDto.duration() != null) fields.put("duration", courseDto.duration());
    if (courseDto.startDate() != null) fields.put("start_date", courseDto.startDate());

    fields.put("last_update_date", LocalDateTime.now());

    if (courseDto.students().size() < MIN_STUDENTS) fields.put("is_open", false);
    else fields.put("is_open", true);

    return fields;
  }

  public CourseDto getCourse(int id) {
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
    if (!courseDtoValidator.isValidForCreate(courseDto)) {
      throw new InvalidRequestDataException("Invalid request data");
    }
    if (courseDto.students().size() > MAX_STUDENTS) {
      throw new CourseIsFullException("Course can't have more than 150 students");
    }
    Course course = courseMapper.toEntity(courseDto);
    course.setLastUpdateDate(LocalDateTime.now());
    boolean isOpen = course.getStudents().size() < MIN_STUDENTS;
    course.setOpen(isOpen);
    return courseMapper.toDto(courseRepository.create(course));
  }

  @Transactional
  public CourseDto updateCourse(CourseDto courseDto) {
    if (!courseDtoValidator.isValidForUpdate(courseDto)) {
      throw new InvalidRequestDataException("Invalid request data");
    }
    Course course =
        courseRepository
            .findById(courseDto.id())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        String.format("Course with id %s not found", courseDto.id())));
    courseRepository.update(course.getId(), getFieldsForUpdate(courseDto));

    if (courseDto.students() != null) {
      if (courseDto.students().size() > MAX_STUDENTS) {
        throw new CourseIsFullException("Course can't have more than 150 students");
      }
      courseRepository.updateCourseStudents(
          course, courseDto.students().stream().map(StudentDto::id).toList());
    }
    return courseMapper.toDto(courseRepository.findById(courseDto.id()).get());
  }

  @Transactional
  public CourseDto deleteCourse(int id) {
    CourseDto courseDto = getCourse(id);
    courseRepository.delete(id);
    return courseDto;
  }
}
