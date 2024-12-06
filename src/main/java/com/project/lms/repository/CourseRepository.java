package com.project.lms.repository;

import com.project.lms.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends
        JpaRepository<Course, Long>,
        QuerydslPredicateExecutor<Course>,
        CourseRepositoryCustom {

    @Query("select c from Course c where c.cName like %:cName% order by " + "c.cId desc")
    List<Course> findBycName(@Param("cName") String cName); // 강의명으로 검색
}