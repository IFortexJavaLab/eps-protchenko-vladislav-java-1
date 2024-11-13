package com.ifortex.internship.repository;

import com.ifortex.internship.model.Student;
import java.util.List;
import java.util.Optional;

/** Repository interface for managing {@link Student} entities. */
public interface StudentRepository {

  /**
   * Finds a student by their unique identifier.
   *
   * @param id the unique identifier of the student
   * @return an {@link Optional} containing the found student, or an empty Optional if no student is
   *     found
   */
  Optional<Student> findById(long id);

  /**
   * Retrieves all students.
   *
   * @return a list of all students
   */
  List<Student> findAll();

  /**
   * Retrieves all students associated with a specific course.
   *
   * @param courseId the unique identifier of the course
   * @return a list of students enrolled in the specified course
   */
  List<Student> findByCourseId(long courseId);

  /**
   * Creates a new student in the repository.
   *
   * @param student the student to be saved
   */
  Student create(Student student);

  /**
   * Updates an existing student's information in the repository.
   *
   * @param student the student with updated information
   */
  void update(Student student);

  /**
   * Deletes a student by their unique identifier.
   *
   * @param id the unique identifier of the student to be deleted
   */
  void delete(long id);
}
