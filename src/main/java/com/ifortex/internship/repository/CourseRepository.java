package com.ifortex.internship.repository;

import com.ifortex.internship.dto.FilterSortDto;
import com.ifortex.internship.model.Course;
import com.ifortex.internship.model.Student;
import com.ifortex.internship.model.enums.CourseField;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Repository interface for managing {@link Course} entities. */
public interface CourseRepository {

  /**
   * Finds a {@link Course} by its unique identifier.
   *
   * @param id the unique identifier of the course
   * @return an {@link Optional} containing the found course, or an empty Optional if no course is
   *     found
   */
  Optional<Course> findById(long id);

  /**
   * Retrieves all {@link Course}s.
   *
   * @return a list of all courses
   */
  List<Course> findAll();

  /**
   * Retrieves {@link Course}s based on applied filters and sorting criteria.
   *
   * @param dto the filter and sort criteria encapsulated in a {@link FilterSortDto} object
   * @return a list of courses matching the filters and sorted according to the provided criteria
   */
  List<Course> findWithFiltersAndSort(FilterSortDto dto);

  /**
   * Creates a new {@link Course} in the repository.
   *
   * @param course the course to be saved
   */
  Course create(Course course);

  /**
   * Deletes a course by its unique identifier.
   *
   * @param courseId the unique identifier of the course to be deleted
   */
  void delete(long courseId);

  /**
   * Updates an existing {@link Course}'s information in the repository.
   *
   * @param courseId the id of the course for which the student associations are updated.
   * @param fields a map of fields that should be updated.
   */
  void update(long courseId, Map<CourseField, Object> fields);

  /**
   * Retrieves a list of existing {@link Student}s whose IDs are passed in the List.
   *
   * @param studentsIds a list of student IDs that should be associated with the course after the
   *     update.
   * @return list of students, that exist in database
   */
  List<Student> getExistingStudents(List<Long> studentsIds);

  /**
   * Updates the {@link Student}s associated with a specific course based on new and existing student IDs.
   *
   * @param course the course for which the student associations are updated.
   * @param newStudentIds a set of student IDs that should be associated with the course after the
   *     update.
   */
  void updateCourseStudents(Course course, List<Long> newStudentIds);
}
