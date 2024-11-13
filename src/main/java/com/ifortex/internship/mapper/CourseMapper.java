package com.ifortex.internship.mapper;

import com.ifortex.internship.dto.CourseDto;
import com.ifortex.internship.model.Course;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourseMapper {

  private final StudentMapper studentMapper;

  public Course toEntity(CourseDto dto) {
    return Course.builder()
        .id(dto.getId())
        .name(dto.getName())
        .description(dto.getDescription())
        .price(dto.getPrice())
        .duration(dto.getDuration())
        .startDate(dto.getStartDate())
        .isOpen(dto.getIsOpen())
        .students(studentMapper.toEntity(dto.getStudents()))
        .build();
  }

  public CourseDto toDto(Course entity) {
    return CourseDto.builder()
        .id(entity.getId())
        .name(entity.getName())
        .description(entity.getDescription())
        .price(entity.getPrice())
        .duration(entity.getDuration())
        .startDate(entity.getStartDate())
        .isOpen(entity.isOpen())
        .students(studentMapper.toDto(entity.getStudents()))
        .build();
  }

  public List<Course> toEntity(List<CourseDto> dtoList) {
    return dtoList.stream().map(this::toEntity).toList();
  }

  public List<CourseDto> toDto(List<Course> entityList) {
    return entityList.stream().map(this::toDto).toList();
  }
}
