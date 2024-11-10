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
        .id(dto.id())
        .name(dto.name())
        .description(dto.description())
        .price(dto.price())
        .duration(dto.duration())
        .startDate(dto.startDate())
        .students(studentMapper.toEntity(dto.students()))
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
