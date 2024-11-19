package com.ifortex.internship.controller;

import com.ifortex.internship.dto.StudentDto;
import com.ifortex.internship.service.StudentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
public class StudentController {

  public final StudentService studentService;

  @GetMapping("/{id}")
  public ResponseEntity<StudentDto> getStudent(@PathVariable("id") long id) {
    return ResponseEntity.ok(studentService.getStudent(id));
  }

  @GetMapping
  public ResponseEntity<List<StudentDto>> getStudents() {
    return ResponseEntity.ok(studentService.getAllStudents());
  }

  @PostMapping
  public ResponseEntity<StudentDto> createStudent(@RequestBody StudentDto studentDto) {
    return ResponseEntity.ok(studentService.createStudent(studentDto));
  }

  @PutMapping("/{id}")
  public ResponseEntity<StudentDto> updateStudent(@PathVariable("id") long id, @RequestBody StudentDto studentDto) {
    studentDto.setId(id);
    return ResponseEntity.ok(studentService.updateStudent(studentDto));
  }

  @DeleteMapping("/{id}")
  public void deleteStudent(@PathVariable("id") long id) {
    studentService.deleteStudent(id);
  }
}
