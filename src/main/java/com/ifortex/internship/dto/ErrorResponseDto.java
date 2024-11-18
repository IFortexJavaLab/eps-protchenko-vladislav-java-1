package com.ifortex.internship.dto;

public record ErrorResponseDto(
        String errorMessage,
        int errorCode
) {}
