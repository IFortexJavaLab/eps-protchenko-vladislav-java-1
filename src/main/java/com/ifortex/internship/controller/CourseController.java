package com.ifortex.internship.controller;

import com.ifortex.internship.dto.CourseDto;
import com.ifortex.internship.dto.FilterSortDto;
import com.ifortex.internship.dto.enums.SortType;
import com.ifortex.internship.service.CourseService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/course")
@RequiredArgsConstructor
public class CourseController {

  private final CourseService courseService;

  @GetMapping("/{id}")
  public ResponseEntity<CourseDto> getCourse(@PathVariable("id") long id) {
    return ResponseEntity.ok(courseService.getCourse(id));
  }

  @GetMapping
  public ResponseEntity<List<CourseDto>> getCoursesWithFilterAndSort(
      @RequestParam(name = "studentName", required = false) String studentName,
      @RequestParam(name = "courseName", required = false) String courseName,
      @RequestParam(name = "courseDesc", required = false) String courseDescription,
      @RequestParam(name = "sortByDate", required = false) SortType sortByDate,
      @RequestParam(name = "sortByName", required = false) SortType sortByName) {
    FilterSortDto dto =
        new FilterSortDto()
            .setStudentName(studentName)
            .setCourseName(courseName)
            .setCourseDescription(courseDescription)
            .setSortByDate(sortByDate)
            .setSortByName(sortByName);
    return ResponseEntity.ok(courseService.getCoursesWithFilterAndSort(dto));
  }

  @PostMapping
  public ResponseEntity<CourseDto> createCourse(@RequestBody CourseDto courseDto) {
    return ResponseEntity.ok(courseService.createCourse(courseDto));
  }

  @PutMapping("/{id}")
  public ResponseEntity<CourseDto> updateCourse(
      @PathVariable("id") long id, @RequestBody CourseDto courseDto) {
    courseDto.setId(id);
    return ResponseEntity.ok(courseService.updateCourse(courseDto));
  }

  @DeleteMapping("/{id}")
  public void deleteCourse(@PathVariable("id") long id) {
    courseService.deleteCourse(id);
  }
}
