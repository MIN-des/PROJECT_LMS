package com.project.lms.service;

import com.project.lms.constant.Dept;
import com.project.lms.dto.StudentDTO;
import com.project.lms.entity.Order;
import com.project.lms.entity.Student;
import com.project.lms.repository.OrderRepository;
import com.project.lms.repository.StudentRepository;
import org.modelmapper.ModelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

	private final StudentRepository studentRepository;
	private final ModelMapper modelMapper;
  private final PasswordEncoder passwordEncoder;
  private final OrderRepository orderRepository;

	@Override
	public Student getStudentInfo(String sId) {
		return studentRepository.findById(sId)
				.orElseThrow(() -> new IllegalArgumentException("학생을 찾을 수 없습니다."));
	}

	@Override
	public Student updateInfo(String sId, StudentDTO updateInfoDTO) {
		Student updateInfo = studentRepository.findById(sId)
				.orElseThrow(() -> new IllegalArgumentException("학생 정보를 찾을 수 없습니다."));

    // 이메일 중복 검사
    if (updateInfoDTO.getSEmail() != null &&
            !updateInfoDTO.getSEmail().equals(updateInfo.getSEmail()) &&
            studentRepository.existsBysEmail(updateInfoDTO.getSEmail())) {
      throw new IllegalArgumentException("이 이메일은 사용하실 수 없습니다.");
    }

    // 전화번호 중복 검사
    if (updateInfoDTO.getSTel() != null &&
            !updateInfoDTO.getSTel().equals(updateInfo.getSTel()) &&
            studentRepository.existsBysTel(updateInfoDTO.getSTel())) {
      throw new IllegalArgumentException("이 전화번호는 사용하실 수 없습니다.");
    }

    if (updateInfoDTO.getSPw() != null && !updateInfoDTO.getSPw().isEmpty()) {
      String encodedPassword = passwordEncoder.encode(updateInfoDTO.getSPw());
      updateInfo.setSPw(encodedPassword);
    }
		updateInfo.setSTel(updateInfoDTO.getSTel() != null ? updateInfoDTO.getSTel() : updateInfo.getSTel());
		updateInfo.setSAdd(updateInfoDTO.getSAdd() != null ? updateInfoDTO.getSAdd() : updateInfo.getSAdd());

		return studentRepository.save(updateInfo);
	}

  @Override
  public Map<String, Object> getOrdersWithScores(String sId) {
    // 학생 확인
    Student student = studentRepository.findById(sId)
            .orElseThrow(() -> new IllegalArgumentException("해당 학생 정보를 찾을 수 없습니다."));

    // 학생의 수강신청 내역 조회
    List<Order> orders = orderRepository.findByStudent_sId(sId);

    // 결과를 Map 형태로 구성
    Map<String, Object> result = new HashMap<>();
    result.put("student", student.getSName()); // 학생 이름
    result.put("orders", orders.stream().map(order -> {
      Map<String, Object> orderDetails = new HashMap<>();
      orderDetails.put("cName", order.getCourse().getCName()); // 강의 이름
      orderDetails.put("score", order.getScore()); // 성적
      orderDetails.put("cId", order.getCourse().getCId()); // 강의 ID
      orderDetails.put("credits", order.getCourse().getCredits());  // 총 학점
      orderDetails.put("pName", order.getCourse().getProfessor().getPName());  // 교수 이름
      orderDetails.put("pDept", order.getCourse().getProfessor().getPDept());
      orderDetails.put("pId", order.getCourse().getProfessor().getPId());

      Integer score = order.getScore();
      String grade;
      if(score != null) {
        if(score >= 90) {
          grade = "A";
        } else if (score >= 80) {
          grade = "B";
        } else if (score >= 70) {
          grade = "C";
        } else {
          grade = "F";
        }
      } else {
        grade = "미부여";
      }
      orderDetails.put("grade", grade);

      return orderDetails;
    }).collect(Collectors.toList()));

    return result;
  }
}