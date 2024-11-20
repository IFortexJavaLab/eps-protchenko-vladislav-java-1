package com.ifortex.internship.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class CourseDto {

  private Long id;
  private String name;
  private String description;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private BigDecimal price;
  private Integer duration;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private LocalDateTime startDate;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private LocalDateTime lastUpdateDate;
  private Boolean isOpen;
  private List<StudentDto> students;
}
