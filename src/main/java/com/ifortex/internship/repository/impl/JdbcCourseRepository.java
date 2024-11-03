package com.ifortex.internship.repository.impl;

import com.ifortex.internship.exception.EntityNotFoundException;
import com.ifortex.internship.model.Course;
import com.ifortex.internship.repository.CourseRepository;
import com.ifortex.internship.repository.StudentRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JdbcCourseRepository implements CourseRepository {

  private final JdbcTemplate jdbcTemplate;
  
  private final StudentRepository studentRepository;

  private final DateTimeFormatter dateTimeFormatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

  private final RowMapper<Course> courseRowMapper =
      ((rs, rowNum) ->
          Course.builder()
              .id(rs.getInt("id"))
              .name(rs.getString("name"))
              .description(rs.getString("description"))
              .price(new BigDecimal(rs.getString("price")))
              .duration(rs.getInt("duration"))
              .startDate(LocalDateTime.parse(rs.getString("start_date"), dateTimeFormatter))
              .lastUpdateDate(
                  LocalDateTime.parse(rs.getString("last_update_date"), dateTimeFormatter))
              .isOpen(rs.getBoolean("is_open"))
              .students(new HashSet<>())
              .build());

  @Override
  public Optional<Course> findById(int id, boolean includeStudents) {
    // TODO: Get everything within single query
    String sql = "SELECT * FROM courses WHERE id = ?";
    Course course =
        jdbcTemplate.query(sql, courseRowMapper, id).stream()
            .findFirst()
                // TODO: remove exception
            .orElseThrow(EntityNotFoundException::new);
    if (includeStudents) {
      course.setStudents(studentRepository.findByCourseId(course.getId()));
    }
    return Optional.ofNullable(course);
  }

  @Override
  public List<Course> findAll(boolean includeStudents) {
    String sql = "SELECT * FROM courses";
    List<Course> courseList = jdbcTemplate.query(sql, courseRowMapper);
    if (includeStudents) {
      courseList.forEach(c -> c.setStudents(studentRepository.findByCourseId(c.getId())));
    }
    return courseList;
  }

  @Override
  public List<Course> findByStudentId(int studentId, boolean includeStudents) {
    String sql =
        "SELECT * FROM courses c JOIN public.m2m_student_course m on c.id = m.course_id WHERE student_id = ?";
    return jdbcTemplate.query(sql, courseRowMapper, studentId);
  }

  @Override
  public void save(Course course) {
    String sql =
        "INSERT INTO courses (name, description, price, duration, start_date, last_update_date, is_open) VALUES (?, ?, ?, ?, ?, ?, ?)";
    jdbcTemplate.update(
        sql,
        course.getName(),
        course.getDescription(),
        course.getPrice(),
        course.getDuration(),
        course.getStartDate(),
        course.getLastUpdateDate(),
        course.isOpen());
    int courseId = jdbcTemplate.queryForObject("SELECT lastval()", Integer.class);
    course.setId(courseId);
    saveCourseStudents(course);
  }

  private void saveCourseStudents(Course course) {
    String sql = "INSERT INTO m2m_student_course (course_id, student_id) VALUES (?, ?)";
    course.getStudents().forEach(s -> jdbcTemplate.update(sql, course.getId(), s.getId()));
  }

  @Override
  public void delete(int courseId) {
    String deleteAssociationsSql = "DELETE FROM m2m_student_course WHERE course_id = ?";
    jdbcTemplate.update(deleteAssociationsSql, courseId);

    String deleteCourseSql = "DELETE FROM courses WHERE id = ?";
    jdbcTemplate.update(deleteCourseSql, courseId);
  }

  @Override
  public void update(Course course) {
    String sql =
        "UPDATE courses SET name = ?, description = ?, price = ?, duration = ?, start_date = ?, last_update_date = ?, is_open = ? WHERE id = ?";
    jdbcTemplate.update(
        sql,
        course.getName(),
        course.getDescription(),
        course.getPrice(),
        course.getDuration(),
        course.getStartDate(),
        course.getLastUpdateDate(),
        course.isOpen(),
        course.getId());
  }

  private void updateCourseStudents(Course course) {
    String deleteSql = "DELETE FROM m2m_student_course WHERE course_id = ?";
    jdbcTemplate.update(deleteSql, course.getId());
    saveCourseStudents(course);
  }
}
