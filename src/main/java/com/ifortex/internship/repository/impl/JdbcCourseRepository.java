package com.ifortex.internship.repository.impl;

import com.ifortex.internship.dto.FilterSortDto;
import com.ifortex.internship.model.Course;
import com.ifortex.internship.model.Student;
import com.ifortex.internship.model.enums.CourseField;
import com.ifortex.internship.repository.CourseRepository;
import com.ifortex.internship.repository.util.CourseWithStudentExtractor;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcCourseRepository implements CourseRepository {

  private final JdbcTemplate jdbcTemplate;

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  private final CourseWithStudentExtractor courseWithStudentExtractor;

  private final RowMapper<Student> studentRowMapper =
      (rs, rowNum) -> Student.builder().id(rs.getLong("id")).name(rs.getString("name")).build();

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
  public Optional<Course> findById(long id) {
    String sql = findAllCoursesSql.concat("\nWHERE c.id = ?");
    List<Course> courses = jdbcTemplate.query(sql, courseWithStudentExtractor, id);
    return courses.stream().findFirst();
  }

  @Override
  public List<Course> findAll() {
    return jdbcTemplate.query(findAllCoursesSql, courseWithStudentExtractor);
  }

  @Override
  public List<Course> findWithFiltersAndSort(FilterSortDto dto) {
    StringBuilder sqlBuilder = new StringBuilder(findAllCoursesSql).append(" WHERE 1=1");
    List<Object> values = new ArrayList<>();

    if (dto.getStudentName() != null) {
      sqlBuilder.append(" AND LOWER(s.name) LIKE LOWER(?)");
      values.add("%" + dto.getStudentName() + "%");
    }
    if (dto.getCourseName() != null) {
      sqlBuilder.append(" AND LOWER(c.name) LIKE LOWER(?)");
      values.add("%" + dto.getCourseName() + "%");
    }
    if (dto.getCourseDescription() != null) {
      sqlBuilder.append(" AND LOWER(c.description) LIKE LOWER(?)");
      values.add("%" + dto.getCourseDescription() + "%");
    }

    boolean hasSort = false;
    if (dto.getSortByDate() != null) {
      sqlBuilder.append(" ORDER BY c.start_date ").append(dto.getSortByDate().name().toUpperCase());
      hasSort = true;
    }
    if (dto.getSortByName() != null) {
      sqlBuilder
          .append(hasSort ? ", " : " ORDER BY ")
          .append("c.name ")
          .append(dto.getSortByName().name().toUpperCase());
    }
    return jdbcTemplate.query(sqlBuilder.toString(), courseWithStudentExtractor, values.toArray());
  }

  @Override
  public Course create(Course course) {
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
    course.setId(keyHolder.getKey().longValue());
    Optional.ofNullable(course.getStudents())
        .ifPresent(
            (list) ->
                saveCourseStudents(course.getId(), list.stream().map(Student::getId).toList()));
    return course;
  }

  @Override
  public void update(long courseId, Map<CourseField, Object> fieldMap) {
    StringBuilder sqlBuilder = new StringBuilder("UPDATE courses SET ");
    List<String> fields = new ArrayList<>();
    List<Object> values = new ArrayList<>();

    fieldMap.forEach(
        (key, value) -> {
          fields.add(key.toString());
          values.add(value);
        });

    sqlBuilder.append(String.join(" = ?, ", fields)).append(" = ? WHERE id = ?");
    values.add(courseId);

    jdbcTemplate.update(sqlBuilder.toString(), values.toArray());
  }

  @Override
  public List<Student> getExistingStudents(List<Long> studentIds) {
    if (studentIds.isEmpty()) {
      return List.of();
    }
    String sql = "SELECT * FROM students WHERE id IN (:studentIds)";
    Map<String, Object> parameters = Map.of("studentIds", studentIds);

    StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM students WHERE id IN (");
    sqlBuilder.append(String.join(", ", studentIds.stream().map(Object::toString).toList()));
    sqlBuilder.append(")");

    return new ArrayList<>(namedParameterJdbcTemplate.query(sql, parameters, studentRowMapper));
  }

  @Override
  public void updateCourseStudents(Course course, List<Long> newStudentIds) {
    String deleteSql = "DELETE FROM m2m_student_course WHERE course_id = ? AND student_id = ?";
    jdbcTemplate.batchUpdate(
        deleteSql,
        course.getStudents(),
        course.getStudents().size(),
        (ps, student) -> {
          ps.setLong(1, course.getId());
          ps.setLong(2, student.getId());
        });

    String insertSql = "INSERT INTO m2m_student_course (course_id, student_id) VALUES (?, ?)";
    jdbcTemplate.batchUpdate(
        insertSql,
        newStudentIds,
        newStudentIds.size(),
        (ps, studentId) -> {
          ps.setLong(1, course.getId());
          ps.setLong(2, studentId);
        });
  }

  @Override
  public void delete(long courseId) {
    String deleteAssociationsSql = "DELETE FROM m2m_student_course WHERE course_id = ?";
    jdbcTemplate.update(deleteAssociationsSql, courseId);

    String deleteCourseSql = "DELETE FROM courses WHERE id = ?";
    jdbcTemplate.update(deleteCourseSql, courseId);
  }

  private void saveCourseStudents(long courseId, List<Long> studentIds) {
    String sql = "INSERT INTO m2m_student_course (course_id, student_id) VALUES (?, ?)";
    jdbcTemplate.batchUpdate(
        sql,
        studentIds,
        studentIds.size(),
        (ps, studentId) -> {
          ps.setLong(1, courseId);
          ps.setLong(2, studentId);
        });
  }
}
