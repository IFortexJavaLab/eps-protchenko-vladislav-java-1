package com.ifortex.internship.repository.impl;

import com.ifortex.internship.model.Student;
import com.ifortex.internship.repository.StudentRepository;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcStudentRepository implements StudentRepository {

  private final JdbcTemplate jdbcTemplate;

  private final RowMapper<Student> studentRowMapper =
      (rs, rowNum) -> Student.builder().id(rs.getInt("id")).name(rs.getString("name")).build();

  @Override
  public Optional<Student> findById(long id) {
    String sql = "SELECT * FROM students WHERE id = ?";
    return jdbcTemplate.query(sql, studentRowMapper, id).stream().findFirst();
  }

  @Override
  public List<Student> findAll() {
    String sql = "SELECT * FROM students";
    return jdbcTemplate.query(sql, studentRowMapper);
  }

  @Override
  public List<Student> findByCourseId(long courseId) {
    String sql =
        "SELECT * FROM students s JOIN public.m2m_student_course m on s.id = m.student_id WHERE course_id = ?";
    return new ArrayList<>(jdbcTemplate.query(sql, studentRowMapper, courseId));
  }

  @Override
  public Student create(Student student) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    String sql = "INSERT INTO students (name) VALUES (?)";
    jdbcTemplate.update(
        connection -> {
          PreparedStatement ps = connection.prepareStatement(sql, new String[] {"id"});
          ps.setString(1, student.getName());
          return ps;
        },
        keyHolder);
    student.setId(keyHolder.getKey().longValue());
    return student;
  }

  @Override
  public void update(Student student) {
    String sql = "UPDATE students SET name = ? WHERE id = ?";
    jdbcTemplate.update(sql, student.getName(), student.getId());
  }

  @Override
  public void delete(long id) {
    String sql = "DELETE FROM students WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }
}
