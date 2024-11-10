package com.ifortex.internship.repository;

import com.ifortex.internship.model.Course;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Repository interface for managing {@link Course} entities. */
public interface CourseRepository {

  /**
   * Finds a course by its unique identifier.
   *
   * @param id the unique identifier of the course
   * @return an {@link Optional} containing the found course, or an empty Optional if no course is
   *     found
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
  Course create(Course course);

  /**
   * Deletes a course by its unique identifier.
   *
   * @param courseId the unique identifier of the course to be deleted
   */
  void delete(int courseId);

  /**
   * Updates an existing course's information in the repository.
   *
   * @param courseId the id of the course for which the student associations are updated.
   * @param fields a map of fields that should be updated.
   */
  void update(int courseId, Map<String, Object> fields);

  /**
   * Updates the students associated with a specific course based on new and existing student IDs.
   *
   * @param course the course for which the student associations are updated.
   * @param newStudentIds a set of student IDs that should be associated with the course after the
   *     update.
   */
  void updateCourseStudents(Course course, List<Integer> newStudentIds);
}
