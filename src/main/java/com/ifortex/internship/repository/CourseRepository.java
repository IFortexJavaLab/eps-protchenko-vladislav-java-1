package com.ifortex.internship.repository;

import com.ifortex.internship.model.Course;

import java.util.List;
import java.util.Optional;

public interface CourseRepository {

  Optional<Course> findById(int id);

  List<Course> findAll();

  void save(Course course);

  void delete(int courseId);

  void update(Course course);
}
