package com.ifortex.internship.repository;

import com.ifortex.internship.model.Course;

import java.util.List;
import java.util.Optional;

public interface CourseRepository {

  Optional<Course> findById(int id, boolean includeStudents);

  List<Course> findAll(boolean includeStudents);

  List<Course> findByStudentId(int id, boolean includeStudents);

  void save(Course course);

  void delete(int courseId);

  void update(Course course);
}
