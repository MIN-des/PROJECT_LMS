//package com.project.lms.service;
//
//import com.project.lms.constant.RestStatus;
//import com.project.lms.dto.CourseDTO;
//import com.project.lms.entity.Course;
//import com.project.lms.repository.CourseRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Commit;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityNotFoundException;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//@SpringBootTest
//@Transactional
//public class CourseServiceTests {
//
//    @Autowired
//    CourseService courseService;
//
//    @Autowired
//    CourseRepository courseRepository;
//
//    @Test
//    @Commit
//    @DisplayName("강의 생성 테스트")
//    public void createCourseTest() throws Exception {
//        CourseDTO courseDTO = new CourseDTO();
//
//        for (int i = 1; i <= 50; i++) {
//            courseDTO.setCName("엔티티 일부 수정 후 등록 " + i);
//            courseDTO.setCredits(3);
//            courseDTO.setStatus(RestStatus.AVAILABLE);
//            courseDTO.setRestNum(25);
//
//            courseService.createCourse(courseDTO);
//        }
////        Long cId = courseService.createCourse(courseDTO);
//
////        Course course = courseRepository.findById(cId).orElseThrow(EntityNotFoundException::new);
//
///*        assertNotNull(course);
//        assertEquals("테스트 강의 2", course.getCName());
//        assertEquals(3, course.getCredits());
//        assertEquals(RestStatus.AVAILABLE, course.getStatus());
//        assertEquals(10, course.getRestNum());*/
//    }
//
//    @Test
//    @Commit
//    @DisplayName("강의 조회 테스트")
//    public void getCourseTest() throws Exception {
//        // Given
//        Course course = new Course();
//        course.setCName("조회 테스트 강의");
//        course.setCredits(2);
//        course.setStatus(RestStatus.AVAILABLE);
//        course.setRestNum(20);
//        course = courseRepository.save(course);
//
////        CourseDTO courseDTO = courseService.getCourse(course.getCId());
//
////        assertNotNull(courseDTO);
////        assertEquals("조회 테스트 강의", courseDTO.getCName());
////        assertEquals(2, courseDTO.getCredits());
////        assertEquals(RestStatus.AVAILABLE, courseDTO.getStatus());
////        assertEquals(20, courseDTO.getRestNum());
//    }
//
//    @Commit
//    @Test
//    @DisplayName("강의 업데이트 테스트")
//    public void updateCourseTest() throws Exception {
//        Course course = new Course();
//        course.setCName("업데이트 전 강의");
//        course.setCredits(1);
//        course.setStatus(RestStatus.FULL);
//        course.setRestNum(10);
//        course = courseRepository.save(course);
//
//        CourseDTO courseDTO = new CourseDTO();
//        courseDTO.setCId(course.getCId()); // 업데이트 대상 ID 설정
//        courseDTO.setCName("업데이트 후 강의");
//        courseDTO.setCredits(4);
//        courseDTO.setStatus(RestStatus.AVAILABLE);
//        courseDTO.setRestNum(50);
//
////        Long updatedCId = courseService.updateCourse(courseDTO);
//
////        Course updatedCourse = courseRepository.findById(updatedCId).orElse(null);
////        assertNotNull(updatedCourse);
////        assertEquals("업데이트 후 강의", updatedCourse.getCName());
////        assertEquals(4, updatedCourse.getCredits());
////        assertEquals(RestStatus.AVAILABLE, updatedCourse.getStatus());
////        assertEquals(50, updatedCourse.getRestNum());
//    }
//
//    @Test
//    @DisplayName("강의 삭제 테스트")
//    public void deleteCourseTest() {
//        long cId = 6;
//        courseRepository.findById(cId).orElseThrow(EntityNotFoundException::new);
//        courseRepository.deleteById(cId);
//    }
//}
