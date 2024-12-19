package com.project.lms.repository;

import com.project.lms.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

  @Transactional
  @Modifying
  @Query("UPDATE Message m SET m.isRead = true WHERE m.receiverId = :receiverId AND m.senderId = :senderId AND m.isRead = false")
  void markMessagesAsRead(@Param("receiverId") String receiverId, @Param("senderId") String senderId);


  // 읽지 않은 메시지 개수를 반환하는 메소드
  long countByReceiverIdAndSenderIdAndIsReadFalse(String receiverId, String senderId);

  // 양방향 채팅 내역 조회
  @Query("SELECT m FROM Message m " +
          "WHERE (m.senderId = :senderId AND m.receiverId = :receiverId) " +
          "   OR (m.senderId = :receiverId AND m.receiverId = :senderId) " +
          "ORDER BY m.sendDate ASC")
  List<Message> findChatHistory(@Param("senderId") String senderId, @Param("receiverId") String receiverId);
}