package com.project.lms.controller;

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
import java.util.List;

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
        return ResponseEntity.ok(chatHistory);
    }

    // 채팅 메인 페이지
    @GetMapping("/main")
    public String chatMainPage(Model model, Principal principal) {
        String userId = principal.getName();

        // 학생이 수강한 교수 목록
        List<Professor> professors = orderService.getProfessorsByStudent(userId);

        // 교수가 담당한 강의를 신청한 학생 목록
        List<Student> students = orderService.getStudentsByProfessor(userId);

        model.addAttribute("professors", professors);
        model.addAttribute("students", students);
        return "message/chatMain";
    }

    @GetMapping("/chat")
    public String chatPage(@RequestParam("receiverId") String receiverId,
                           @RequestParam("receiverName") String receiverName,
                           Model model, Principal principal) {

        String senderId = principal.getName(); // 로그인한 사용자 ID
        List<MessageDTO> chatHistory = messageService.getChatHistory(senderId, receiverId);

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
