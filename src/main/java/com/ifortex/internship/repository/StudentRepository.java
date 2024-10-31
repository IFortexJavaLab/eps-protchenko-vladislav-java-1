package com.ifortex.internship.repository;

import com.ifortex.internship.model.Student;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface StudentRepository {

  Optional<Student> findById(int id);

  List<Student> findAll();

  Set<Student> findByCourseId(int courseId);

  void save(Student student);

  void update(Student student);

  void delete(int id);
}
