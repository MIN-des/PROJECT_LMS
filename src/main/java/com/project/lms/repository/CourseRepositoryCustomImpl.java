package com.project.lms.repository;

import com.project.lms.constant.RestStatus;
import com.project.lms.dto.CourseSearchDTO;
import com.project.lms.dto.MainCourseDTO;
import com.project.lms.entity.Course;
import com.project.lms.entity.QCourse;
import com.project.lms.dto.QMainCourseDTO;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class CourseRepositoryCustomImpl implements CourseRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public CourseRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    // 잔여 여부로 검색
    private BooleanExpression searchRestStatusEq(RestStatus searchRestStatus) {
        return searchRestStatus == null ? null : QCourse.course.status.eq(searchRestStatus);
    }

    // 특정 날짜로 검색
    private BooleanExpression regDtsAfter(String searchDateType) {
        LocalDateTime dateTime = LocalDateTime.now();

        if (StringUtils.equals("all", searchDateType) || searchDateType == null) {
            return null;
        } else if (StringUtils.equals("1d", searchDateType)) {
            dateTime = dateTime.minusDays(1);
        } else if (StringUtils.equals("1w", searchDateType)) {
            dateTime = dateTime.minusWeeks(1);
        } else if (StringUtils.equals("1m", searchDateType)) {
            dateTime = dateTime.minusMonths(1);
        } else if (StringUtils.equals("6m", searchDateType)) {
            dateTime = dateTime.minusMonths(6);
        }

        return QCourse.course.regTime.after(dateTime);
    }

    // 특정 조건으로 검색
    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if (StringUtils.equals("cName", searchBy)) {
            return QCourse.course.cName.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("createBy", searchBy)) { // createBy는 추후 로그인된 사용자 정보로 검색
            return QCourse.course.createdBy.like("%" + searchQuery + "%");
        }

        return null;
    }

    @Override
    public Page<Course> getProfessorCoursePage(CourseSearchDTO searchDTO, Pageable pageable) {
        QueryResults<Course> results = queryFactory.selectFrom(QCourse.course)
                .where(regDtsAfter(searchDTO.getSearchDateType()),
                        searchRestStatusEq(searchDTO.getSearchRestStatus()),
                        searchByLike(searchDTO.getSearchBy(), searchDTO.getSearchQuery()))
                .orderBy(QCourse.course.cId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Course> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    /*private BooleanExpression cNameLike(String searchQuery) {
        return StringUtils.isEmpty(searchQuery) ? null : QCourse.course.cName.like("%" + searchQuery + "%");
    }

    @Override
    public Page<MainCourseDTO> getMainCoursePage(CourseSearchDTO searchDTO, Pageable pageable) {
        QCourse course = QCourse.course;

        QueryResults<MainCourseDTO> results = queryFactory.select(
                        new QMainCourseDTO(
                                course.cId,
                                course.cName,
                                course.credits,
                                course.restNum,
                                course.createdBy
                        ))
                .from(course)
                .where(
                        searchByLike(searchDTO.getSearchBy(), searchDTO.getSearchQuery()), // 검색 조건
                        regDtsAfter(searchDTO.getSearchDateType()) // 등록일 조건
                )
                .offset(pageable.getOffset()) // 페이징 시작 위치
                .limit(pageable.getPageSize()) // 페이지 크기
                .orderBy(course.cId.desc()) // 정렬 조건 (예: 최신순 정렬)
                .fetchResults(); // 결과 조회 및 전체 개수 계산

        // 페이징을 위한 Page 객체 생성
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }*/
}