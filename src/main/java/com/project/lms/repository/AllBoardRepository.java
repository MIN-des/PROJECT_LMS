package com.project.lms.repository.admin;

import com.project.lms.entity.AllBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AllBoardRepository extends JpaRepository<AllBoard, Long> {

    @Query(value = "select now()", nativeQuery = true)
    String getTime();

}