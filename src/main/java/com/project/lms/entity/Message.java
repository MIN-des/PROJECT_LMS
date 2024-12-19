package com.project.lms.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mId;       // 메시지 고유 ID (조회용)

    private String senderId;      // 발신자 ID
    private String senderName;
    private String receiverId;    // 수신자 ID
    private String receiverName;
    private String content;       // 메시지 내용
    private LocalDateTime sendDate; // 전송 일시 (조회용)

    // 추가함
    private boolean isRead = false; // 읽음 여부

    // 메시지 생성자
    public Message(String senderId, String senderName, String receiverId, String receiverName, String content) {
        this.senderId = senderId;
        this.senderName = senderName;
        this.receiverId = receiverId;
        this.receiverName = receiverName;
        this.content = content;
        this.sendDate = LocalDateTime.now();
    }
}
