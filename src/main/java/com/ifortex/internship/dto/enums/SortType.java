package com.ifortex.internship.dto.enums;

public enum SortType {
  ASC("ASC"),
  DESC("DESC");

  private final String value;

  SortType(String name) {
    this.value = name;
  }
}
