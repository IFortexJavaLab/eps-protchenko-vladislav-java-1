package com.ifortex.internship.repository.impl;

import com.ifortex.internship.model.Course;
import com.ifortex.internship.model.Student;
import com.ifortex.internship.repository.CourseRepository;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JdbcCourseRepository implements CourseRepository {

  private final JdbcTemplate jdbcTemplate;

  private final DateTimeFormatter dateTimeFormatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

  private final CourseWithStudentExtractor courseWithStudentExtractor;

  @Override
  public Optional<Course> findById(int id) {
    String sql =
        """
        SELECT c.id as course_id, c.name as course_name, c.description as course_description,
               c.price as course_price, c.duration as course_duration,
               c.start_date as course_start_date, c.last_update_date as course_last_update_date,
               c.is_open as course_is_open,
               s.id as student_id, s.name as student_name
        FROM courses c
        LEFT JOIN m2m_student_course m ON c.id = m.course_id
        LEFT JOIN students s ON m.student_id = s.id
        WHERE c.id = ?
        """;
    List<Course> courses = jdbcTemplate.query(sql, courseWithStudentExtractor, id);
    return courses.stream().findFirst();
  }

  @Override
  public List<Course> findAll() {
    String sql =
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
    return jdbcTemplate.query(sql, courseWithStudentExtractor);
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
    jdbcTemplate.batchUpdate(
        sql,
        course.getStudents(),
        course.getStudents().size(),
        (ps, student) -> {
          ps.setInt(1, course.getId());
          ps.setInt(2, student.getId());
        });
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
    updateCourseStudents(course);
  }

  private void updateCourseStudents(Course course) {
    String selectSql = "SELECT student_id FROM m2m_student_course WHERE course_id = ?";
    Set<Integer> existingStudentIds =
        new HashSet<>(
            jdbcTemplate.query(selectSql, (rs, rowNum) -> rs.getInt("student_id"), course.getId()));

    Set<Integer> newStudentIds =
        course.getStudents().stream().map(Student::getId).collect(Collectors.toSet());

    Set<Integer> studentsToAdd = new HashSet<>(existingStudentIds);
    studentsToAdd.removeAll(existingStudentIds);

    Set<Integer> studentsToRemove = new HashSet<>(newStudentIds);
    studentsToAdd.removeAll(newStudentIds);

    String insertSql = "INSERT INTO m2m_student_course (course_id, student_id) VALUES (?, ?)";
    jdbcTemplate.batchUpdate(
        insertSql,
        studentsToAdd,
        studentsToAdd.size(),
        (ps, studentId) -> {
          ps.setInt(1, course.getId());
          ps.setInt(2, studentId);
        });

    String deleteSql = "DELETE FROM m2m_student_course WHERE course_id = ? AND student_id = ?";
    jdbcTemplate.batchUpdate(
        deleteSql,
        studentsToRemove,
        studentsToRemove.size(),
        (ps, studentId) -> {
          ps.setInt(1, course.getId());
          ps.setInt(2, studentId);
        });
  }
}
