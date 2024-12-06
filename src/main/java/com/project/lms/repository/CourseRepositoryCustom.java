package com.project.lms.repository;

import com.project.lms.dto.CourseSearchDTO;
import com.project.lms.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseRepositoryCustom {

    Page<Course> getProfessorCoursePage(CourseSearchDTO searchDTO, Pageable pageable);

//    Page<MainCourseDTO> getMainCoursePage(CourseSearchDTO searchDTO, Pageable pageable);
}
