package com.project.lms.service;

import com.project.lms.dto.OrderDTO;
import com.project.lms.entity.Course;
import com.project.lms.entity.Order;
import com.project.lms.entity.Professor;
import com.project.lms.entity.Student;

import java.util.List;
import java.util.Map;

public interface OrderService {

  // 학생이 수강 신청한 교수 목록 가져오기
  List<Professor> getProfessorsByStudent(String sId);

  // 교수가 담당하는 강의를 수강 신청한 학생 목록 가져오기
  List<Student> getStudentsByProfessor(String pId);

  // 학점 부여
  void updateScore(Long oId, Integer score);

  List<Order> getOrdersByCourse_cId(Long cId);

  OrderDTO createOrder(OrderDTO orderDTO);

  // Order -> OrderDTO 변환
  List<OrderDTO> getOrdersByStudent_sId(String sId);

  void cancelOrder(Long oId);
}
