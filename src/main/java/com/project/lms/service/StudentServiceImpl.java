package com.project.lms.service;

import com.project.lms.constant.Dept;
import com.project.lms.dto.StudentDTO;
import com.project.lms.entity.Student;
import com.project.lms.repository.StudentRepository;
import org.modelmapper.ModelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private ModelMapper modelMapper;
  private final PasswordEncoder passwordEncoder;

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

    updateInfo.setSName(updateInfoDTO.getSName() != null ? updateInfoDTO.getSName() : updateInfo.getSName());
    if (updateInfoDTO.getSPw() != null && !updateInfoDTO.getSPw().isEmpty()) {
      String encodedPassword = passwordEncoder.encode(updateInfoDTO.getSPw());
      updateInfo.setSPw(encodedPassword);
    }
		updateInfo.setSTel(updateInfoDTO.getSTel() != null ? updateInfoDTO.getSTel() : updateInfo.getSTel());
		updateInfo.setSAdd(updateInfoDTO.getSAdd() != null ? updateInfoDTO.getSAdd() : updateInfo.getSAdd());
		updateInfo.setSBirth(updateInfoDTO.getSBirth() != null ? updateInfoDTO.getSBirth() : updateInfo.getSBirth());
		updateInfo.setSEmail(updateInfoDTO.getSEmail() != null ? updateInfoDTO.getSEmail() : updateInfo.getSEmail());
		updateInfo.setGrade(updateInfoDTO.getGrade() > 0 ? updateInfoDTO.getGrade() : updateInfo.getGrade());
		updateInfo.setSDept(updateInfoDTO.getSDept() != null ? updateInfoDTO.getSDept() : updateInfo.getSDept());
		updateInfo.setSGen(updateInfoDTO.getSGen() != null ? updateInfoDTO.getSGen() : updateInfo.getSGen());
		updateInfo.setRole(updateInfoDTO.getRole() != null ? updateInfoDTO.getRole() : updateInfo.getRole());

		return studentRepository.save(updateInfo);
	}
}