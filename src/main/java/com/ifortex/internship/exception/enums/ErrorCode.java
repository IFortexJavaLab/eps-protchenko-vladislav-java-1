package com.ifortex.internship.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    STUDENT_NOT_FOUND(40401),

    INVALID_STUDENT_REQUEST_DATA(40601),

    COURSE_NOT_FOUND(40402),

    INVALID_COURSE_REQUEST_DATA(40602),

    COURSE_HAS_ALREADY_STARTED(40602),

    COURSE_IS_CLOSED(40602),

    COURSE_IS_FULL(40602);

    private final int value;
}
