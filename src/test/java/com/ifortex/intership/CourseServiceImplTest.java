package com.ifortex.intership;

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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
    Mockito.when(courseRepository.findById(1L)).thenReturn(Optional.of(mockCourse));
    Mockito.when(courseMapper.toDto(mockCourse)).thenReturn(mockCourseDto);

    CourseDto result = courseService.getCourse(1L);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(mockCourseDto.getId(), result.getId());
    Assertions.assertEquals(mockCourseDto.getName(), result.getName());
    Mockito.verify(courseRepository).findById(1L);
    Mockito.verify(courseMapper).toDto(mockCourse);
  }

  @Test
  void testGetCourse_ShouldThrowEntityNotFoundException_WhenCourseDoesNotExist() {
    Mockito.when(courseRepository.findById(1L)).thenReturn(Optional.empty());

    EntityNotFoundException exception =
        Assertions.assertThrows(EntityNotFoundException.class, () -> courseService.getCourse(1L));

    Assertions.assertEquals("Course with id 1 not found", exception.getMessage());
    Mockito.verify(courseRepository).findById(1L);
  }

  @Test
  void testCreateCourse_ShouldReturnCreatedCourse() {
    Mockito.when(courseMapper.toEntity(mockCourseDto)).thenReturn(mockCourse);
    Mockito.when(courseRepository.create(mockCourse)).thenReturn(mockCourse);
    Mockito.when(courseMapper.toDto(mockCourse)).thenReturn(mockCourseDto);

    CourseDto result = courseService.createCourse(mockCourseDto);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(mockCourseDto.getId(), result.getId());
    Mockito.verify(courseDtoValidator).validateForCreate(mockCourseDto);
    Mockito.verify(courseMapper).toEntity(mockCourseDto);
    Mockito.verify(courseRepository).create(mockCourse);
    Mockito.verify(courseMapper).toDto(mockCourse);
  }

  @Test
  void testUpdateCourse_ShouldThrowEntityNotFoundException_WhenCourseDoesNotExist() {
    Mockito.when(courseRepository.findById(mockCourseDto.getId())).thenReturn(Optional.empty());

    EntityNotFoundException exception =
        Assertions.assertThrows(
            EntityNotFoundException.class, () -> courseService.updateCourse(mockCourseDto));

    Assertions.assertEquals("Course with id 1 not found", exception.getMessage());
    Mockito.verify(courseRepository).findById(mockCourseDto.getId());
  }

  @Test
  void testDeleteCourse_ShouldDeleteCourse_WhenCourseExists() {
    Mockito.when(courseRepository.findById(1L)).thenReturn(Optional.of(mockCourse));

    courseService.deleteCourse(1L);

    Mockito.verify(courseRepository).findById(1L);
    Mockito.verify(courseRepository).delete(1L);
  }

  @Test
  void testDeleteCourse_ShouldThrowEntityNotFoundException_WhenCourseDoesNotExist() {
    Mockito.when(courseRepository.findById(1L)).thenReturn(Optional.empty());

    EntityNotFoundException exception =
        Assertions.assertThrows(
            EntityNotFoundException.class, () -> courseService.deleteCourse(1L));

    Assertions.assertEquals("Course with id 1 not found", exception.getMessage());
    Mockito.verify(courseRepository).findById(1L);
  }

  @Test
  void testGetCoursesWithFilterAndSort_ShouldReturnFilteredAndSortedCourses() {
    FilterSortDto filterSortDto = new FilterSortDto();
    List<Course> courseList = List.of(mockCourse);
    List<CourseDto> courseDtoList = List.of(mockCourseDto);

    Mockito.when(courseRepository.findWithFiltersAndSort(filterSortDto)).thenReturn(courseList);
    Mockito.when(courseMapper.toDto(courseList)).thenReturn(courseDtoList);

    List<CourseDto> result = courseService.getCoursesWithFilterAndSort(filterSortDto);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(1, result.size());
    Assertions.assertEquals(mockCourseDto.getId(), result.get(0).getId());
    Mockito.verify(courseRepository).findWithFiltersAndSort(filterSortDto);
    Mockito.verify(courseMapper).toDto(courseList);
  }
}
