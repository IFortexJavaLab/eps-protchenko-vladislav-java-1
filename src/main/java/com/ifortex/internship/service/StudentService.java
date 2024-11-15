package com.ifortex.internship.service;

import com.ifortex.internship.dto.StudentDto;
import com.ifortex.internship.exception.EntityNotFoundException;
import com.ifortex.internship.exception.InvalidRequestDataException;
import com.ifortex.internship.model.Student;

import java.util.List;

/**
 * Service interface for managing {@link Student} entities in the application.
 * Provides methods for retrieving, creating, updating, and deleting student records.
 */
public interface StudentService {

  /**
   * Retrieves a student by their unique identifier.
   *
   * @param id the unique identifier of the student
   * @return a {@link StudentDto} representing the student details
   * @throws EntityNotFoundException if the student with the specified ID is not found
   */
  StudentDto getStudent(long id);

  /**
   * Retrieves a list of all students.
   *
   * @return a list of {@link StudentDto} objects representing all students
   */
  List<StudentDto> getAllStudents();

  /**
   * Creates a new student.
   *
   * @param studentDto the {@link StudentDto} object containing student details to create
   * @return the created {@link StudentDto} object with its generated unique identifier
   * @throws InvalidRequestDataException if the student data is invalid
   */
  StudentDto createStudent(StudentDto studentDto);

  /**
   * Updates an existing student.
   *
   * @param studentDto the {@link StudentDto} object containing updated student details
   * @return the updated {@link StudentDto} object
   * @throws EntityNotFoundException if the student with the specified ID does not exist
   * @throws InvalidRequestDataException if the student data is invalid
   */
  StudentDto updateStudent(StudentDto studentDto);

  /**
   * Deletes a student by their unique identifier.
   *
   * @param id the unique identifier of the student to delete
   * @throws EntityNotFoundException if the student with the specified ID does not exist
   */
  void deleteStudent(long id);
}

