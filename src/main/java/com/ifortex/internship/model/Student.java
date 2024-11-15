package com.ifortex.internship.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
public class Student {

  @EqualsAndHashCode.Exclude
  private Long id;
  private String name;
}
