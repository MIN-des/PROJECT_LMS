package com.project.lms.service;

import com.project.lms.dto.CourseDTO;
import com.project.lms.dto.CourseSearchDTO;
import com.project.lms.dto.MainCourseDTO;
import com.project.lms.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourseService {

    public Long createCourse(CourseDTO courseDTO) throws Exception;

    public CourseDTO getCourse(Long cId) throws Exception;

    public Long updateCourse(CourseDTO courseDTO) throws Exception;
}
