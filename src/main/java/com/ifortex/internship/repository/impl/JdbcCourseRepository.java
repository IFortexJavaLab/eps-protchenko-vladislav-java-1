package com.ifortex.internship.repository.impl;

import com.ifortex.internship.model.Course;
import com.ifortex.internship.model.Student;
import com.ifortex.internship.repository.CourseRepository;
import com.ifortex.internship.repository.utils.CourseWithStudentExtractor;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JdbcCourseRepository implements CourseRepository {

  private final JdbcTemplate jdbcTemplate;

  private final CourseWithStudentExtractor courseWithStudentExtractor;

  private final String findAllCoursesSql =
      """
          SELECT c.id as course_id, c.name as course_name, c.description as course_description,
                 c.price as course_price, c.duration as course_duration,
                 c.start_date as course_start_date, c.last_update_date as course_last_update_date,
                 c.is_open as course_is_open,
                 s.id as student_id, s.name as student_name
          FROM courses c
          LEFT JOIN m2m_student_course m ON c.id = m.course_id
          LEFT JOIN students s ON m.student_id = s.id
          """;

  @Override
  public Optional<Course> findById(int id) {
    String sql = findAllCoursesSql.concat("\nWHERE c.id = ?");
    List<Course> courses = jdbcTemplate.query(sql, courseWithStudentExtractor, id);
    return courses.stream().findFirst();
  }

  @Override
  public List<Course> findAll() {
    return jdbcTemplate.query(findAllCoursesSql, courseWithStudentExtractor);
  }

  @Override
  public int create(Course course) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    String sql =
        "INSERT INTO courses (name, description, price, duration, start_date, last_update_date, is_open) VALUES (?, ?, ?, ?, ?, ?, ?)";
    jdbcTemplate.update(
        connection -> {
          PreparedStatement ps = connection.prepareStatement(sql, new String[] {"id"});
          ps.setObject(1, course.getName());
          ps.setObject(2, course.getDescription());
          ps.setObject(3, course.getPrice());
          ps.setObject(4, course.getDuration());
          ps.setObject(5, course.getStartDate());
          ps.setObject(6, course.getLastUpdateDate());
          ps.setObject(7, course.isOpen());
          return ps;
        },
        keyHolder);
    int courseId = keyHolder.getKey().intValue();
    saveCourseStudents(courseId, course.getStudents().stream().map(Student::getId).toList());
    return courseId;
  }

  private void saveCourseStudents(int courseId, List<Integer> studentIds) {
    String sql = "INSERT INTO m2m_student_course (course_id, student_id) VALUES (?, ?)";
    jdbcTemplate.batchUpdate(
        sql,
        studentIds,
        studentIds.size(),
        (ps, studentId) -> {
          ps.setInt(1, courseId);
          ps.setInt(2, studentId);
        });
  }

  @Override
  public void update(int courseId, Map<String, Object> fields) {
    StringBuilder sqlBuilder = new StringBuilder("UPDATE courses SET ");
    List<Object> values = new ArrayList<>();

    fields.forEach(
        (key, value) -> {
          sqlBuilder.append(key).append(" = ?, ");
          values.add(value);
        });

    sqlBuilder.setLength(sqlBuilder.length() - 2);
    sqlBuilder.append(" WHERE id = ?");
    values.add(courseId);

    jdbcTemplate.update(sqlBuilder.toString(), values.toArray());
  }

  @Override
  public void updateCourseStudents(Course course, List<Integer> newStudentIds) {
    String deleteSql = "DELETE FROM m2m_student_course WHERE course_id = ? AND student_id = ?";
    jdbcTemplate.batchUpdate(
        deleteSql,
        course.getStudents(),
        course.getStudents().size(),
        (ps, student) -> {
          ps.setInt(1, course.getId());
          ps.setInt(2, student.getId());
        });

    String insertSql = "INSERT INTO m2m_student_course (course_id, student_id) VALUES (?, ?)";
    jdbcTemplate.batchUpdate(
        insertSql,
        newStudentIds,
        newStudentIds.size(),
        (ps, studentId) -> {
          ps.setInt(1, course.getId());
          ps.setInt(2, studentId);
        });
  }

  @Override
  public void delete(int courseId) {
    String deleteAssociationsSql = "DELETE FROM m2m_student_course WHERE course_id = ?";
    jdbcTemplate.update(deleteAssociationsSql, courseId);

    String deleteCourseSql = "DELETE FROM courses WHERE id = ?";
    jdbcTemplate.update(deleteCourseSql, courseId);
  }
}
