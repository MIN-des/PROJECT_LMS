package com.project.lms.controller;

import com.project.lms.dto.OrderDTO;
import com.project.lms.dto.MessageDTO;
import com.project.lms.entity.Professor;
import com.project.lms.entity.Student;
import com.project.lms.repository.ProfessorRepository;
import com.project.lms.repository.StudentRepository;
import com.project.lms.service.MessageService;
import com.project.lms.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/message")
public class MessageController {

  private final MessageService messageService;
  private final OrderService orderService;

  private final ProfessorRepository professorRepository;
  private final StudentRepository studentRepository;

  @PostMapping("/api/send")
  @ResponseBody
  public ResponseEntity<?> sendMessage(@RequestParam("receiverId") String receiverId,
                                       @RequestParam("content") String content,
                                       Principal principal) {
    String senderId = principal.getName();
    String senderName = getSenderName(senderId); // 발신자 이름 조회 메서드


    MessageDTO messageDTO = new MessageDTO();
    messageDTO.setSenderId(senderId);
    messageDTO.setSenderName(senderName);
    messageDTO.setReceiverId(receiverId);
    messageDTO.setContent(content);
    messageDTO.setSendDate(messageDTO.getSendDate());

    messageService.sendMessage(messageDTO);
    return ResponseEntity.ok().build();
  }

  private String getSenderName(String senderId) {
    if (senderId.startsWith("S")) { // 학생인 경우
      return studentRepository.findById(senderId)
              .map(Student::getSName)
              .orElse("Unknown Sender");
    } else if (senderId.startsWith("P")) { // 교수인 경우
      return professorRepository.findById(senderId)
              .map(Professor::getPName)
              .orElse("Unknown Sender");
    } else {
      return "Unknown Sender";
    }
  }

  @GetMapping("/api/history/{receiverId}")
  @ResponseBody
  public ResponseEntity<List<MessageDTO>> getChatHistory(@PathVariable String receiverId,
                                                         Principal principal) {
    String senderId = principal.getName();
    List<MessageDTO> chatHistory = messageService.getChatHistory(senderId, receiverId);

    if (chatHistory.isEmpty()) {
      return ResponseEntity.noContent().build(); // HTTP 204 반환
    }

    return ResponseEntity.ok(chatHistory); // 채팅 내역 반환
  }

  // 채팅 메인 페이지
  @GetMapping("/main")
  public String chatMainPage(Model model, Principal principal) {
    String userId = principal.getName(); // 로그인한 교수의 ID

    // 학생이 수강한 교수 목록과 읽지 않은 메시지 개수 (학생 전용)
    List<Map<String, Object>> professorsWithUnreadCount = orderService.getProfessorsByStudent(userId)
            .stream()
            .map(prof -> {
              Map<String, Object> data = new HashMap<>();
              data.put("pId", prof.getPId());
              data.put("pName", prof.getPName());
              data.put("unreadCount", messageService.getUnreadMessageCount(userId, prof.getPId()));
              data.put("courses", prof.getCourses()); // 교수의 강의 목록
              return data;
            }).collect(Collectors.toList());

    // 로그인한 교수가 담당하는 학생 목록과 해당 강의명만 필터링
    List<Map<String, Object>> studentsWithUnreadCount = orderService.getStudentsByProfessor(userId)
            .stream()
            .map(student -> {
              Map<String, Object> data = new HashMap<>();
              data.put("sId", student.getSId());
              data.put("sName", student.getSName());

              // 해당 학생이 로그인한 교수의 강의에 신청한 강의명만 필터링
              List<String> courseNames = orderService.getOrdersByStudent_sId(student.getSId())
                      .stream()
                      .filter(order -> order.getPId().equals(userId)) // 교수의 ID와 일치하는 강의만 필터링
                      .map(OrderDTO::getCName) // 강의명 가져오기
                      .collect(Collectors.toList());

              data.put("courses", courseNames);
              data.put("unreadCount", messageService.getUnreadMessageCount(userId, student.getSId()));
              return data;
            })
            .filter(student -> !((List<String>) student.get("courses")).isEmpty()) // 해당 강의가 있는 학생만 표시
            .collect(Collectors.toList());

    model.addAttribute("professors", professorsWithUnreadCount);
    model.addAttribute("students", studentsWithUnreadCount);

    return "message/chatMain";
  }

  @GetMapping("/chat")
  public String chatPage(@RequestParam("receiverId") String receiverId,
                         @RequestParam("receiverName") String receiverName,
                         Model model,
                         Principal principal) {

    String senderId = principal.getName(); // 로그인한 사용자 ID
    List<MessageDTO> chatHistory = messageService.getChatHistory(senderId, receiverId);

    // 읽지 않은 메시지 읽음 처리
    messageService.markMessagesAsRead(senderId, receiverId);

    model.addAttribute("chatHistory", chatHistory);
    model.addAttribute("receiverId", receiverId);
    model.addAttribute("receiverName", receiverName);
    return "message/chat";
  }

  @PostMapping("/send")
  public String sendMessage(@RequestParam("receiverId") String receiverId,
                            @RequestParam("receiverName") String receiverName,
                            @RequestParam("content") String content,
                            Principal principal,
                            RedirectAttributes redirectAttributes) {

    String senderId = principal.getName(); // 로그인된 사용자 ID
    String senderName = getSenderName(senderId); // 발신자 이름 조회 메서드

    // MessageDTO 생성
    MessageDTO messageDTO = new MessageDTO();
    messageDTO.setSenderId(senderId);
    messageDTO.setSenderName(senderName);
    messageDTO.setReceiverId(receiverId);
    messageDTO.setReceiverName(receiverName);
    messageDTO.setContent(content);

    messageService.sendMessage(messageDTO);

    redirectAttributes.addAttribute("receiverId", receiverId);
    redirectAttributes.addAttribute("receiverName", receiverName);
    return "redirect:/message/chat";
  }


}
