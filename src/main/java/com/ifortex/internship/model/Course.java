package com.ifortex.internship.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
public class Course {

  @EqualsAndHashCode.Exclude
  private int id;
  private String name;
  private String description;
  private BigDecimal price;
  private int duration;
  private LocalDateTime startDate;
  private LocalDateTime lastUpdateDate;
  private boolean isOpen;
  private Set<Student> students;
}
