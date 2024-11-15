package com.ifortex.internship.mapper;

import com.ifortex.internship.dto.CourseDto;
import com.ifortex.internship.model.Course;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourseMapper {

  private final StudentMapper studentMapper;

  public Course toEntity(CourseDto dto) {
    Course course =
        Course.builder()
            .id(dto.getId())
            .name(dto.getName())
            .description(dto.getDescription())
            .price(dto.getPrice())
            .duration(dto.getDuration())
            .startDate(dto.getStartDate())
            .isOpen(dto.getIsOpen())
            .build();
    Optional.ofNullable(dto.getStudents())
        .ifPresent(list -> course.setStudents(studentMapper.toEntity(list)));
    return course;
  }

  public CourseDto toDto(Course entity) {
    CourseDto dto =
        new CourseDto()
            .setId(entity.getId())
            .setName(entity.getName())
            .setDescription(entity.getDescription())
            .setPrice(entity.getPrice())
            .setDuration(entity.getDuration())
            .setStartDate(entity.getStartDate())
            .setLastUpdateDate(entity.getLastUpdateDate())
            .setIsOpen(entity.isOpen());
    dto.setStudents(
        studentMapper.toDto(Optional.ofNullable(entity.getStudents()).orElse(List.of())));
    return dto;
  }

  public List<Course> toEntity(List<CourseDto> dtoList) {
    return dtoList.stream().map(this::toEntity).toList();
  }

  public List<CourseDto> toDto(List<Course> entityList) {
    return entityList.stream().map(this::toDto).toList();
  }
}
