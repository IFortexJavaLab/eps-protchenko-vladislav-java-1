package com.ifortex.internship.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
public class CourseDto {

  private Long id;
  private String name;
  private String description;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private BigDecimal price;

  private Integer duration;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private LocalDateTime startDate;

  @JsonIgnore
  private LocalDateTime lastUpdateDate;
  private Boolean isOpen;
  private List<StudentDto> students;
}
