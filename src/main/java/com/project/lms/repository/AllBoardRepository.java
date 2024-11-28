package com.project.lms.repository;

import com.project.lms.entity.AllBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllBoardRepository extends JpaRepository<AllBoard, Long> {
}