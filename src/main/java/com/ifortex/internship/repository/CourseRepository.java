package com.ifortex.internship.repository;

import com.ifortex.internship.model.Course;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Course entities.
 */
public interface CourseRepository {

  /**
   * Finds a course by its unique identifier.
   *
   * @param id the unique identifier of the course
   * @return an {@link Optional} containing the found course, or an empty Optional if no course is found
   */
  Optional<Course> findById(int id);

  /**
   * Retrieves all courses.
   *
   * @return a list of all courses
   */
  List<Course> findAll();

  /**
   * Creates a new course in the repository.
   *
   * @param course the course to be saved
   */
  void create(Course course);

  /**
   * Deletes a course by its unique identifier.
   *
   * @param courseId the unique identifier of the course to be deleted
   */
  void delete(int courseId);

  /**
   * Updates an existing course's information in the repository.
   *
   * @param course the course with updated information
   */
  void update(Course course);
}
