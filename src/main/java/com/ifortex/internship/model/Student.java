package com.ifortex.internship.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Student {

  private int id;
  private String name;
}
