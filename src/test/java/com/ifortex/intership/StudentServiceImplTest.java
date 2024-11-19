package com.ifortex.intership;

import com.ifortex.internship.dto.StudentDto;
import com.ifortex.internship.exception.EntityNotFoundException;
import com.ifortex.internship.mapper.StudentMapper;
import com.ifortex.internship.model.Student;
import com.ifortex.internship.repository.StudentRepository;
import com.ifortex.internship.service.impl.StudentServiceImpl;
import com.ifortex.internship.service.validator.StudentDtoValidator;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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

    Mockito.when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
    Mockito.when(studentMapper.toDto(student)).thenReturn(studentDto);

    StudentDto result = studentService.getStudent(studentId);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(studentId, result.getId());
    Mockito.verify(studentRepository).findById(studentId);
    Mockito.verify(studentMapper).toDto(student);
  }

  @Test
  void testGetStudent_ShouldThrowException_WhenNotFound() {
    long studentId = 1L;

    Mockito.when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

    EntityNotFoundException exception =
        Assertions.assertThrows(
            EntityNotFoundException.class, () -> studentService.getStudent(studentId));

    Assertions.assertEquals("Student with id 1 not found", exception.getMessage());
    Mockito.verify(studentRepository).findById(studentId);
    Mockito.verifyNoInteractions(studentMapper);
  }

  @Test
  void testGetAllStudents_ShouldReturnListOfStudents() {
    List<Student> studentList = List.of(Student.builder().build());
    List<StudentDto> studentDtoList = List.of(new StudentDto());

    Mockito.when(studentRepository.findAll()).thenReturn(studentList);
    Mockito.when(studentMapper.toDto(studentList)).thenReturn(studentDtoList);

    List<StudentDto> result = studentService.getAllStudents();

    Assertions.assertNotNull(result);
    Assertions.assertEquals(studentDtoList.size(), result.size());
    Mockito.verify(studentRepository).findAll();
    Mockito.verify(studentMapper).toDto(studentList);
  }

  @Test
  void testCreateStudent_ShouldValidateAndSaveStudent() {
    StudentDto studentDto = new StudentDto();
    Student student = Student.builder().build();

    Mockito.when(studentMapper.toEntity(studentDto)).thenReturn(student);
    Mockito.when(studentRepository.create(student)).thenReturn(student);
    Mockito.when(studentMapper.toDto(student)).thenReturn(studentDto);

    StudentDto result = studentService.createStudent(studentDto);

    Assertions.assertNotNull(result);
    Mockito.verify(studentDtoValidator).validate(studentDto);
    Mockito.verify(studentMapper).toEntity(studentDto);
    Mockito.verify(studentRepository).create(student);
    Mockito.verify(studentMapper).toDto(student);
  }

  @Test
  void testUpdateStudent_ShouldThrowException_WhenStudentNotFound() {
    StudentDto studentDto = new StudentDto();
    studentDto.setId(1L);

    Mockito.when(studentRepository.findById(studentDto.getId())).thenReturn(Optional.empty());

    EntityNotFoundException exception =
        Assertions.assertThrows(
            EntityNotFoundException.class, () -> studentService.updateStudent(studentDto));

    Assertions.assertEquals("Student with id 1 not found", exception.getMessage());
    Mockito.verify(studentDtoValidator).validate(studentDto);
    Mockito.verify(studentRepository).findById(studentDto.getId());
    Mockito.verifyNoMoreInteractions(studentRepository);
  }

  @Test
  void testDeleteStudent_ShouldThrowException_WhenStudentNotFound() {
    long studentId = 1L;

    Mockito.when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

    EntityNotFoundException exception =
        Assertions.assertThrows(
            EntityNotFoundException.class, () -> studentService.deleteStudent(studentId));

    Assertions.assertEquals("Student with id 1 not found", exception.getMessage());
    Mockito.verify(studentRepository).findById(studentId);
    Mockito.verifyNoMoreInteractions(studentRepository);
  }
}
