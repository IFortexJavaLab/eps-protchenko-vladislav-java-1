package com.ifortex.internship.mapper;

import com.ifortex.internship.dto.StudentDto;
import com.ifortex.internship.model.Student;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class StudentMapper {

  public Student toEntity(StudentDto dto) {
    return Student.builder()
        .id(Optional.ofNullable(dto.getId()).orElse(0L))
        .name(dto.getName())
        .build();
  }

  public StudentDto toDto(Student entity) {
    return new StudentDto().setId(entity.getId()).setName(entity.getName());
  }

  public List<Student> toEntity(List<StudentDto> dtoList) {
    return dtoList.stream().map(this::toEntity).toList();
  }

  public List<StudentDto> toDto(List<Student> entityList) {
    return entityList.stream().map(this::toDto).toList();
  }
}
