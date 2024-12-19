package com.project.lms.repository.admin;

import com.project.lms.entity.Files;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<Files, Long> {
  // 파일 정보를 저장하고 관리하기 위한 기본적인 메서드를 JpaRepository가 제공
  Optional<Files> findByBnoAndUuid(Long bno, String uuid);  // 게시글 번호와 UUID로 파일 조회
}
