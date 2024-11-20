package com.ifortex.intership;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ifortex.internship.dto.CourseDto;
import com.ifortex.internship.dto.FilterSortDto;
import com.ifortex.internship.exception.EntityNotFoundException;
import com.ifortex.internship.mapper.CourseMapper;
import com.ifortex.internship.model.Course;
import com.ifortex.internship.repository.CourseRepository;
import com.ifortex.internship.service.impl.CourseServiceImpl;
import com.ifortex.internship.service.validator.CourseDtoValidator;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CourseServiceImplTest {

  @Mock private CourseRepository courseRepository;

  @Mock private CourseMapper courseMapper;

  @Mock private CourseDtoValidator courseDtoValidator;

  @InjectMocks private CourseServiceImpl courseService;

  private Course mockCourse;

  private CourseDto mockCourseDto;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    mockCourse =
        Course.builder()
            .id(1L)
            .name("Math")
            .lastUpdateDate(LocalDateTime.now())
            .students(List.of())
            .build();
    mockCourseDto =
        new CourseDto()
            .setId(1L)
            .setName("Math")
            .setLastUpdateDate(LocalDateTime.now())
            .setStudents(List.of());
  }

  @Test
  void testGetCourse_ShouldReturnCourse_WhenCourseExists() {
    when(courseRepository.findById(1L)).thenReturn(Optional.of(mockCourse));
    when(courseMapper.toDto(mockCourse)).thenReturn(mockCourseDto);

    CourseDto result = courseService.getCourse(1L);

    assertNotNull(result);
    assertEquals(mockCourseDto.getId(), result.getId());
    assertEquals(mockCourseDto.getName(), result.getName());
    verify(courseRepository).findById(1L);
    verify(courseMapper).toDto(mockCourse);
  }

  @Test
  void testGetCourse_ShouldThrowEntityNotFoundException_WhenCourseDoesNotExist() {
    when(courseRepository.findById(1L)).thenReturn(Optional.empty());

    EntityNotFoundException exception =
        assertThrows(EntityNotFoundException.class, () -> courseService.getCourse(1L));

    assertEquals("Course with id 1 not found", exception.getMessage());
    verify(courseRepository).findById(1L);
  }

  @Test
  void testCreateCourse_ShouldReturnCreatedCourse() {
    when(courseMapper.toEntity(mockCourseDto)).thenReturn(mockCourse);
    when(courseRepository.create(mockCourse)).thenReturn(mockCourse);
    when(courseMapper.toDto(mockCourse)).thenReturn(mockCourseDto);

    CourseDto result = courseService.createCourse(mockCourseDto);

    assertNotNull(result);
    assertEquals(mockCourseDto.getId(), result.getId());
    verify(courseDtoValidator).validateForCreate(mockCourseDto);
    verify(courseMapper).toEntity(mockCourseDto);
    verify(courseRepository).create(mockCourse);
    verify(courseMapper).toDto(mockCourse);
  }

  @Test
  void testUpdateCourse_ShouldThrowEntityNotFoundException_WhenCourseDoesNotExist() {
    when(courseRepository.findById(mockCourseDto.getId())).thenReturn(Optional.empty());

    EntityNotFoundException exception =
        assertThrows(
            EntityNotFoundException.class, () -> courseService.updateCourse(mockCourseDto));

    assertEquals("Course with id 1 not found", exception.getMessage());
    verify(courseRepository).findById(mockCourseDto.getId());
  }

  @Test
  void testDeleteCourse_ShouldDeleteCourse_WhenCourseExists() {
    when(courseRepository.findById(1L)).thenReturn(Optional.of(mockCourse));

    courseService.deleteCourse(1L);

    verify(courseRepository).findById(1L);
    verify(courseRepository).delete(1L);
  }

  @Test
  void testDeleteCourse_ShouldThrowEntityNotFoundException_WhenCourseDoesNotExist() {
    when(courseRepository.findById(1L)).thenReturn(Optional.empty());

    EntityNotFoundException exception =
        assertThrows(
            EntityNotFoundException.class, () -> courseService.deleteCourse(1L));

    assertEquals("Course with id 1 not found", exception.getMessage());
    verify(courseRepository).findById(1L);
  }

  @Test
  void testGetCoursesWithFilterAndSort_ShouldReturnFilteredAndSortedCourses() {
    FilterSortDto filterSortDto = new FilterSortDto();
    List<Course> courseList = List.of(mockCourse);
    List<CourseDto> courseDtoList = List.of(mockCourseDto);

    when(courseRepository.findWithFiltersAndSort(filterSortDto)).thenReturn(courseList);
    when(courseMapper.toDto(courseList)).thenReturn(courseDtoList);

    List<CourseDto> result = courseService.getCoursesWithFilterAndSort(filterSortDto);

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(mockCourseDto.getId(), result.get(0).getId());
    verify(courseRepository).findWithFiltersAndSort(filterSortDto);
    verify(courseMapper).toDto(courseList);
  }
}
