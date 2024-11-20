package com.ifortex.intership;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.ifortex.internship.dto.StudentDto;
import com.ifortex.internship.exception.EntityNotFoundException;
import com.ifortex.internship.mapper.StudentMapper;
import com.ifortex.internship.model.Student;
import com.ifortex.internship.repository.StudentRepository;
import com.ifortex.internship.service.impl.StudentServiceImpl;
import com.ifortex.internship.service.validator.StudentDtoValidator;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class StudentServiceImplTest {

  @Mock private StudentRepository studentRepository;

  @Mock private StudentMapper studentMapper;

  @Mock private StudentDtoValidator studentDtoValidator;

  @InjectMocks private StudentServiceImpl studentService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetStudent_ShouldReturnStudent_WhenExists() {
    long studentId = 1L;
    Student student = Student.builder().id(studentId).build();
    StudentDto studentDto = new StudentDto().setId(studentId);

    when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
    when(studentMapper.toDto(student)).thenReturn(studentDto);

    StudentDto result = studentService.getStudent(studentId);

    assertNotNull(result);
    assertEquals(studentId, result.getId());
    verify(studentRepository).findById(studentId);
    verify(studentMapper).toDto(student);
  }

  @Test
  void testGetStudent_ShouldThrowException_WhenNotFound() {
    long studentId = 1L;

    when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

    EntityNotFoundException exception =
        assertThrows(
            EntityNotFoundException.class, () -> studentService.getStudent(studentId));

    assertEquals("Student with id 1 not found", exception.getMessage());
    verify(studentRepository).findById(studentId);
    verifyNoInteractions(studentMapper);
  }

  @Test
  void testGetAllStudents_ShouldReturnListOfStudents() {
    List<Student> studentList = List.of(Student.builder().build());
    List<StudentDto> studentDtoList = List.of(new StudentDto());

    when(studentRepository.findAll()).thenReturn(studentList);
    when(studentMapper.toDto(studentList)).thenReturn(studentDtoList);

    List<StudentDto> result = studentService.getAllStudents();

    assertNotNull(result);
    assertEquals(studentDtoList.size(), result.size());
    verify(studentRepository).findAll();
    verify(studentMapper).toDto(studentList);
  }

  @Test
  void testCreateStudent_ShouldValidateAndSaveStudent() {
    StudentDto studentDto = new StudentDto();
    Student student = Student.builder().build();

    when(studentMapper.toEntity(studentDto)).thenReturn(student);
    when(studentRepository.create(student)).thenReturn(student);
    when(studentMapper.toDto(student)).thenReturn(studentDto);

    StudentDto result = studentService.createStudent(studentDto);

    assertNotNull(result);
    verify(studentDtoValidator).validate(studentDto);
    verify(studentMapper).toEntity(studentDto);
    verify(studentRepository).create(student);
    verify(studentMapper).toDto(student);
  }

  @Test
  void testUpdateStudent_ShouldThrowException_WhenStudentNotFound() {
    StudentDto studentDto = new StudentDto();
    studentDto.setId(1L);

    when(studentRepository.findById(studentDto.getId())).thenReturn(Optional.empty());

    EntityNotFoundException exception =
        assertThrows(
            EntityNotFoundException.class, () -> studentService.updateStudent(studentDto));

    assertEquals("Student with id 1 not found", exception.getMessage());
    verify(studentDtoValidator).validate(studentDto);
    verify(studentRepository).findById(studentDto.getId());
    verifyNoMoreInteractions(studentRepository);
  }

  @Test
  void testDeleteStudent_ShouldThrowException_WhenStudentNotFound() {
    long studentId = 1L;

    when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

    EntityNotFoundException exception =
        assertThrows(
            EntityNotFoundException.class, () -> studentService.deleteStudent(studentId));

    assertEquals("Student with id 1 not found", exception.getMessage());
    verify(studentRepository).findById(studentId);
    verifyNoMoreInteractions(studentRepository);
  }
}
