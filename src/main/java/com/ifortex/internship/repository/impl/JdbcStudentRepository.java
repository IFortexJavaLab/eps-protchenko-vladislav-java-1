package com.ifortex.internship.repository.impl;

import com.ifortex.internship.model.Student;
import com.ifortex.internship.repository.StudentRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JdbcStudentRepository implements StudentRepository {

  private final JdbcTemplate jdbcTemplate;

  private final RowMapper<Student> studentRowMapper =
      (rs, rowNum) -> Student.builder().id(rs.getInt("id")).name(rs.getString("name")).build();

  @Override
  public Optional<Student> findById(int id) {
    String sql = "SELECT * FROM students WHERE id = ?";
    return jdbcTemplate.query(sql, studentRowMapper, id).stream().findFirst();
  }

  @Override
  public List<Student> findAll() {
    String sql = "SELECT * FROM students";
    return jdbcTemplate.query(sql, studentRowMapper);
  }

  @Override
  public List<Student> findByCourseId(int courseId) {
    String sql =
        "SELECT * FROM students WHERE id IN (SELECT student_id FROM m2m_student_course WHERE course_id = ?)";
    return jdbcTemplate.query(sql, studentRowMapper, courseId);
  }

  @Override
  public void save(Student student) {
    String sql = "INSERT INTO students (name) VALUES (?)";
    jdbcTemplate.update(sql, student.getName());
  }

  @Override
  public void update(Student student) {
    String sql = "UPDATE students SET name = ? WHERE id = ?";
    jdbcTemplate.update(sql, student.getName(), student.getId());
  }

  @Override
  public void delete(int id) {
    String sql = "DELETE FROM students WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }
}
