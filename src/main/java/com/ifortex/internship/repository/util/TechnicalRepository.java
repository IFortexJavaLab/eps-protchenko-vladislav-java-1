package com.ifortex.internship.repository.util;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TechnicalRepository {

  private final JdbcTemplate jdbcTemplate;

  public void updateCourseState() {
    String sql = "CALL close_today_courses()";
    jdbcTemplate.update(sql);
  }
}
