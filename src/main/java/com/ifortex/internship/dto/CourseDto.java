package com.ifortex.internship.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record CourseDto(
    int id,
    String name,
    String description,
    @JsonFormat(shape = JsonFormat.Shape.STRING) BigDecimal price,
    Integer duration,
    @JsonFormat(shape = JsonFormat.Shape.STRING) LocalDateTime startDate,
    List<StudentDto> students) {}
