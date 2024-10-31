package com.ifortex.internship.repository;

import com.ifortex.internship.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository {

  Optional<Student> findById(int id);

  List<Student> findAll();

  List<Student> findByCourseId(int courseId);

  void save(Student student);

  void update(Student student);

  void delete(int id);
}
