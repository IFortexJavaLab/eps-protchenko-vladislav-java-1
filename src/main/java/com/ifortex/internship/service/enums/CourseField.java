package com.ifortex.internship.service.enums;

public enum CourseField {
    NAME("name"),
    DESCRIPTION("description"),
    PRICE("price"),
    DURATION("duration"),
    START_DATE("start_date"),
    LAST_UPDATE_DATE("last_update_date"),
    IS_OPEN("is_open");

    private final String name;

    CourseField(String name) {
        this.name = name;
    }

}
