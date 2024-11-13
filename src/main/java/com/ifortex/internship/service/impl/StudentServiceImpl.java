package com.ifortex.internship.service.impl;

import com.ifortex.internship.dto.StudentDto;
import com.ifortex.internship.exception.EntityNotFoundException;
import com.ifortex.internship.mapper.StudentMapper;
import com.ifortex.internship.model.Student;
import com.ifortex.internship.repository.StudentRepository;
import com.ifortex.internship.service.StudentService;
import com.ifortex.internship.service.validator.StudentDtoValidator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

  private final StudentRepository studentRepository;

  private final StudentMapper studentMapper;

  private final StudentDtoValidator studentDtoValidator;

  @Override
  public StudentDto getStudent(long id) {
    return studentMapper.toDto(
        studentRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        String.format("Student with id=%d not found", id))));
  }

  @Override
  public List<StudentDto> getAllStudents() {
    return studentMapper.toDto(studentRepository.findAll());
  }

  @Transactional
  @Override
  public StudentDto createStudent(StudentDto studentDto) {
    studentDtoValidator.validate(studentDto);
    Student student = studentRepository.create(studentMapper.toEntity(studentDto));
    return studentMapper.toDto(student);
  }

  @Transactional
  @Override
  public StudentDto updateStudent(StudentDto studentDto) {
    studentDtoValidator.validate(studentDto);
    if (studentRepository.findById(studentDto.getId()).isEmpty()) {
      throw new EntityNotFoundException(String.format("Student with id=%d not found", studentDto.getId()));
    }
    studentRepository.update(studentMapper.toEntity(studentDto));
    return studentDto;
  }

  @Transactional
  @Override
  public StudentDto deleteStudent(long id) {
    StudentDto studentDto = getStudent(id);
    studentRepository.delete(id);
    return studentDto;
  }
}
