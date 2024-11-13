package com.ifortex.internship.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
public class Course {

  @EqualsAndHashCode.Exclude
  private long id;
  private String name;
  private String description;
  private BigDecimal price;
  private int duration;
  private LocalDateTime startDate;
  private LocalDateTime lastUpdateDate;
  private boolean isOpen;
  private List<Student> students;

}
