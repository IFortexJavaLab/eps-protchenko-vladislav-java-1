package com.ifortex.internship.service.util;

import com.ifortex.internship.repository.util.TechnicalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourseUtil {

  private final TechnicalRepository repository;

  @Scheduled(cron = "0 0 0 * * ?")
  public void scheduled() {
    repository.updateCourseState();
  }
}
