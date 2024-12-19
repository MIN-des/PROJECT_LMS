package com.project.lms.service;

import com.project.lms.dto.MessageDTO;
import com.project.lms.entity.Message;
import com.project.lms.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    @Transactional
    public void markMessagesAsRead(String receiverId, String senderId) {
        // 특정 발신자와 수신자 간의 읽지 않은 메시지를 읽음 처리
        messageRepository.markMessagesAsRead(receiverId, senderId);
    }

    @Transactional(readOnly = true)
    public Long getUnreadMessageCount(String receiverId, String senderId) {
        // 읽지 않은 메시지 개수 조회
        return messageRepository.countByReceiverIdAndSenderIdAndIsReadFalse(receiverId, senderId);
    }

    @Transactional
    public void sendMessage(MessageDTO messageDTO) {
        // Message 엔티티 생성
        Message message = new Message(
                messageDTO.getSenderId(),
                messageDTO.getSenderName(), // 발신자 이름
                messageDTO.getReceiverId(),
                messageDTO.getReceiverName(), // 수신자 이름
                messageDTO.getContent()
        );

        messageRepository.save(message);
    }

    @Transactional(readOnly = true)
    public List<MessageDTO> getChatHistory(String senderId, String receiverId) {
        // 양방향 메시지 조회
        List<Message> messages = messageRepository.findChatHistory(senderId, receiverId);

        return messages.stream()
                .map(msg -> new MessageDTO(
                        msg.getMId(),
                        msg.getSenderId(),
                        msg.getSenderName(), // 발신자 이름
                        msg.getReceiverId(),
                        msg.getReceiverName(), // 수신자 이름
                        msg.getContent(),
                        msg.getSendDate()))
                .collect(Collectors.toList());
    }
}
