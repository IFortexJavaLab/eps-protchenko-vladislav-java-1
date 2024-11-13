package com.ifortex.internship.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class StudentDto {

  private Long id;
  private String name;
}
