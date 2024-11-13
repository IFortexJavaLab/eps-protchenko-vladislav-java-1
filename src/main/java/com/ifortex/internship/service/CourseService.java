package com.ifortex.internship.service;

import com.ifortex.internship.dto.CourseDto;
import com.ifortex.internship.exception.CourseHasAlreadyStartedException;
import com.ifortex.internship.exception.CourseIsFullException;
import com.ifortex.internship.exception.EntityNotFoundException;
import com.ifortex.internship.exception.InvalidRequestDataException;
import com.ifortex.internship.model.Course;

import java.util.List;

/**
 * Service interface for managing {@link Course} entities in the application. Provides methods for
 * retrieving, creating, updating, and deleting course records.
 */
public interface CourseService {

  /**
   * Retrieves a course by its unique identifier.
   *
   * @param id the unique identifier of the course
   * @return a {@link CourseDto} representing the course details
   * @throws EntityNotFoundException if the course with the specified ID is not found
   */
  CourseDto getCourse(long id);

  /**
   * Retrieves a list of all courses.
   *
   * @return a list of {@link CourseDto} objects representing all courses
   */
  List<CourseDto> getAllCourses();

  /**
   * Creates a new course.
   *
   * @param courseDto the {@link CourseDto} object containing course details to create
   * @return the created {@link CourseDto} object with its generated unique identifier
   * @throws InvalidRequestDataException if the course data is invalid
   * @throws CourseIsFullException if the student list consists of more than 150 students
   */
  CourseDto createCourse(CourseDto courseDto);

  /**
   * Updates an existing course.
   *
   * @param courseDto the {@link CourseDto} object containing updated course details
   * @return the updated {@link CourseDto} object
   * @throws EntityNotFoundException if the course with the specified ID does not exist
   * @throws InvalidRequestDataException if the course data is invalid
   * @throws CourseIsFullException if the student list consists of more than 150 students
   * @throws CourseHasAlreadyStartedException when trying to open closed course or update course
   *     students after start date
   */
  CourseDto updateCourse(CourseDto courseDto);

  /**
   * Deletes a course by its unique identifier.
   *
   * @param id the unique identifier of the course to delete
   * @return the deleted {@link CourseDto} object representing the course details
   * @throws EntityNotFoundException if the course with the specified ID does not exist
   */
  CourseDto deleteCourse(long id);
}
