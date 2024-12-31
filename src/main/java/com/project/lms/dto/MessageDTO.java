package com.project.lms.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 모든 필드를 포함한 생성자
public class MessageDTO {

  private Long mId;       // 메시지 고유 ID (조회용)
  private String senderId;      // 발신자 ID
  private String senderName;
  private String receiverId;    // 수신자 ID
  private String receiverName;
  private String content;       // 메시지 내용
  private LocalDateTime sendDate; // 전송 일시 (조회용)

  // 채팅 페이지에서 사용되는 추가 생성자
  public MessageDTO(String senderId, String receiverId, String content, LocalDateTime sendDate) {
    this.senderId = senderId;
    this.receiverId = receiverId;
    this.content = content;
    this.sendDate = sendDate;
  }

  // 채팅 조회 시 사용되는 생성자 (발신자, 수신자 이름 포함)
  public MessageDTO(String senderId, String senderName,
                    String receiverId, String receiverName,
                    String content, LocalDateTime sendDate) {
    this.senderId = senderId;
    this.senderName = senderName;
    this.receiverId = receiverId;
    this.receiverName = receiverName;
    this.content = content;
    this.sendDate = sendDate;
  }
}
