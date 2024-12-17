package com.project.lms.repository;


import com.project.lms.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // 양방향 채팅 내역 조회
    @Query("SELECT m FROM Message m " +
            "WHERE (m.senderId = :senderId AND m.receiverId = :receiverId) " +
            "   OR (m.senderId = :receiverId AND m.receiverId = :senderId) " +
            "ORDER BY m.sendDate ASC")
    List<Message> findChatHistory(@Param("senderId") String senderId, @Param("receiverId") String receiverId);
}