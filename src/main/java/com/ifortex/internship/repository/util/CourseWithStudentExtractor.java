package com.ifortex.internship.repository.util;

import com.ifortex.internship.model.Course;
import com.ifortex.internship.model.Student;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

@Component
public class CourseWithStudentExtractor implements ResultSetExtractor<List<Course>> {

  @Override
  public List<Course> extractData(ResultSet rs) throws SQLException, DataAccessException {
    Map<Long, Course> courseMap = new LinkedHashMap<>();

    while (rs.next()) {
      long courseId = rs.getInt("course_id");
      Course course = courseMap.get(courseId);
      if (course == null) {
        course =
            Course.builder()
                .id(courseId)
                .name(rs.getString("course_name"))
                .description(rs.getString("course_description"))
                .price(rs.getBigDecimal("course_price"))
                .duration(rs.getInt("course_duration"))
                .startDate(rs.getObject("course_start_date", LocalDateTime.class))
                .lastUpdateDate(rs.getObject("course_last_update_date", LocalDateTime.class))
                .isOpen(rs.getBoolean("course_is_open"))
                .students(new ArrayList<>())
                .build();
        courseMap.put(course.getId(), course);
      }

      long studentId = rs.getInt("student_id");
      if (studentId != 0) {
        Student student =
            Student.builder().id(studentId).name(rs.getString("student_name")).build();
        course.getStudents().add(student);
      }
    }

    return new ArrayList<>(courseMap.values());
  }
}
