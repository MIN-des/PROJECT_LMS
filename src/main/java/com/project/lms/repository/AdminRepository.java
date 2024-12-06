package com.project.lms.repository;

import com.project.lms.entity.Admin;
import com.project.lms.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {
}
