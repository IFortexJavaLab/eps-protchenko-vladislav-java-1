package com.ifortex.internship.service;

import com.ifortex.internship.dto.StudentDto;
import com.ifortex.internship.exception.EntityNotFoundException;
import com.ifortex.internship.exception.InvalidRequestDataException;
import com.ifortex.internship.mapper.StudentMapper;
import com.ifortex.internship.model.Student;
import com.ifortex.internship.repository.StudentRepository;
import com.ifortex.internship.service.validator.StudentDtoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

  private final StudentRepository studentRepository;

  private final StudentMapper studentMapper;

  private final StudentDtoValidator studentDtoValidator;

  public StudentDto getStudent(int id) {
    return studentMapper.toDto(
        studentRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        String.format("Student with id=%d not found", id))));
  }

  public List<StudentDto> getAllStudents() {
    return studentMapper.toDto(studentRepository.findAll());
  }

  @Transactional
  public StudentDto createStudent(StudentDto studentDto) {
    if (!studentDtoValidator.isValid(studentDto)) {
      throw new InvalidRequestDataException("Invalid request data");
    }
    Student student = studentRepository.create(studentMapper.toEntity(studentDto));
    return studentMapper.toDto(student);
  }

  @Transactional
  public StudentDto updateStudent(StudentDto studentDto) {
    if (!studentDtoValidator.isValid(studentDto)) {
      throw new InvalidRequestDataException("Invalid request data");
    }
    Student student =
        studentRepository
            .findById(studentDto.id())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        String.format("Student with id=%d not found", studentDto.id())));
    studentRepository.update(studentMapper.toEntity(studentDto));
    return studentDto;
  }

  @Transactional
  public StudentDto deleteStudent(int id) {
    StudentDto studentDto = getStudent(id);
    studentRepository.delete(id);
    return studentDto;
  }
}
