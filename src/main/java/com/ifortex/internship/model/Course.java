package com.ifortex.internship.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Course {

    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;
    private LocalDateTime startDate;
    private LocalDateTime lastUpdateDate;
    private Boolean isOpen;
    private Set<Student> students;
}
