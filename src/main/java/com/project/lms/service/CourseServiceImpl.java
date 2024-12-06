package com.project.lms.service;

import com.project.lms.dto.CourseDTO;
import com.project.lms.dto.CourseSearchDTO;
import com.project.lms.dto.MainCourseDTO;
import com.project.lms.entity.Course;
import com.project.lms.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    // 강의 생성 메소드
    @Override
    public Long createCourse(CourseDTO courseDTO) throws Exception {
        Course course = courseDTO.createCourse();
        courseRepository.save(course);

        return course.getCId();
    }

    // 강의 조회 메소드
    @Transactional(readOnly = true)
    @Override
    public CourseDTO getCourse(Long cId) throws Exception {
        Course course = courseRepository.findById(cId).orElseThrow(EntityNotFoundException::new);
        CourseDTO courseDTO = CourseDTO.of(course);

        return courseDTO;
    }

    // 강의 업데이트 메소드
    @Override
    public Long updateCourse(CourseDTO courseDTO) throws Exception {
        Course course = courseRepository.findById(courseDTO.getCId()).orElseThrow(EntityNotFoundException::new);
        course.updateCourse(courseDTO);

        return course.getCId();
    }

    // 강의 수정, 삭제 버튼이 보이는 페이지
    @Transactional(readOnly = true)
    public Page<Course> getProfessorCoursePage(CourseSearchDTO searchDTO, Pageable pageable) {
        return courseRepository.getProfessorCoursePage(searchDTO, pageable);
    }

    // 강의 신청 버튼이 보이는 페이지
    /*@Transactional(readOnly = true)
    public Page<MainCourseDTO> getMainCoursePage(CourseSearchDTO searchDTO, Pageable pageable) {
        return courseRepository.getMainCoursePage(searchDTO, pageable);
    }*/
}
