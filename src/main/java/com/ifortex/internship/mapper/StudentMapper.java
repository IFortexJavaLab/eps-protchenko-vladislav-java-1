package com.ifortex.internship.mapper;

import com.ifortex.internship.dto.StudentDto;
import com.ifortex.internship.model.Student;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StudentMapper {

  public Student toEntity(StudentDto dto) {
    return Student.builder().id(dto.id()).name(dto.name()).build();
  }

  public StudentDto toDto(Student entity) {
    return StudentDto.builder().id(entity.getId()).name(entity.getName()).build();
  }

  public List<Student> toEntity(List<StudentDto> dtoList) {
    return dtoList.stream().map(this::toEntity).toList();
  }

  public List<StudentDto> toDto(List<Student> entityList) {
    return entityList.stream().map(this::toDto).toList();
  }
}
