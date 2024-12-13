package com.project.lms.service;

import com.project.lms.constant.Dept;
import com.project.lms.dto.ProfessorDTO;
import com.project.lms.dto.StudentDTO;
import com.project.lms.entity.Admin;
import com.project.lms.entity.Professor;
import com.project.lms.entity.Student;
import com.project.lms.entity.TuitionInvoiceUpload;
import com.project.lms.repository.AdminRepository;
import com.project.lms.repository.ProfessorRepository;
import com.project.lms.repository.StudentRepository;
import com.project.lms.repository.TuitionInvoiceUploadRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{

	private final AdminRepository adminRepository;
	private final ProfessorRepository professorRepository;
	private final PasswordEncoder passwordEncoder;
	private final ModelMapper modelMapper;
	private final StudentRepository studentRepository;
	private final TuitionInvoiceUploadRepository uploadRepository;

	@Override
	public ProfessorDTO createProfessor(ProfessorDTO professorDTO) {
		if (professorRepository.existsById(professorDTO.getPId())) {
			throw new IllegalArgumentException("이미 중복된 교직원 번호가 존재합니다.");
		}

		if (professorRepository.existsBypEmail(professorDTO.getPEmail())) {
			throw new IllegalArgumentException("이미 중복된 이메일이 존재합니다.");
		}
		// 비밀번호 암호화
		professorDTO.setPPw(passwordEncoder.encode(professorDTO.getPPw()));

		Professor professor = modelMapper.map(professorDTO, Professor.class);
		return modelMapper.map(professorRepository.save(professor), ProfessorDTO.class);
	}

	// 관리자, 교수가 교수 세부정보 조회
	@Override
	public Optional<ProfessorDTO> getProfessorById(String pId) {
		return professorRepository.findById(pId)
				.map(professor -> modelMapper.map(professor, ProfessorDTO.class));
	}

	// 관리자가 교수 정보 업데이트
	@Override
	public ProfessorDTO updateProfessor(String pId, ProfessorDTO professorDTO) {
		Professor professor = professorRepository.findById(pId)
				.orElseThrow(() -> new EntityNotFoundException("Professor with id " + pId + " not found"));

		// 교직원 번호 중복 확인
		if (!professor.getPId().equals(professorDTO.getPId()) && professorRepository.existsById(professorDTO.getPId())) {
			throw new IllegalArgumentException("이미 중복된 교직원 번호가 존재합니다: " + professorDTO.getPId());
		}

		// 이메일 중복 확인
		if (!professor.getPEmail().equals(professorDTO.getPEmail()) && professorRepository.existsBypEmail(professorDTO.getPEmail())) {
			throw new IllegalArgumentException("이미 중복된 이메일이 존재합니다: " + professorDTO.getPEmail());
		}

		modelMapper.map(professorDTO, professor);
		return modelMapper.map(professorRepository.save(professor), ProfessorDTO.class);
	}

	@Override
	public void deleteProfessor(String pId) {
		professorRepository.deleteById(pId);
	}

	@Override
	public Page<ProfessorDTO> searchProfessorsById(String pId, Pageable pageable) {
		return professorRepository.findBypIdContainingIgnoreCase(pId, pageable)
				.map(professor -> modelMapper.map(professor, ProfessorDTO.class));
	}

	@Override
	public Page<ProfessorDTO> searchProfessorsByName(String pName, Pageable pageable) {
		return professorRepository.findBypNameContainingIgnoreCase(pName, pageable)
				.map(professor -> modelMapper.map(professor, ProfessorDTO.class));
	}

	@Override
	public Page<ProfessorDTO> searchProfessorsByDept(String pDept, Pageable pageable) {
		try {
			// pDept 를 Enum 타입으로 변환
			Dept dept = Dept.valueOf(pDept.toUpperCase()); // 유효하지 않은 값이면 예외 발생
			return professorRepository.findBypDept(dept, pageable) // 변환된 Dept 타입 사용
					.map(professor -> modelMapper.map(professor, ProfessorDTO.class));
		} catch (IllegalArgumentException e) {
			// 변환 실패 시 예외 처리
			throw new IllegalArgumentException("유효하지 않은 학부 값입니다.");
		}
	}

	@Override
	public Page<ProfessorDTO> getAllProfessors(Pageable pageable) {
		return professorRepository.findAll(pageable)
				.map(professor -> modelMapper.map(professor, ProfessorDTO.class));
	}



	@Override
	public StudentDTO createStudent(StudentDTO studentDTO) {
		if (studentRepository.existsById(studentDTO.getSId())) {
			throw new IllegalArgumentException("이미 중복된 학번이 존재합니다.");
		}
		if (studentRepository.existsBysEmail(studentDTO.getSEmail())) {
			throw new IllegalArgumentException("이미 중복된 이메일이 존재합니다.");
		}
		// 비밀번호 암호화
		studentDTO.setSPw(passwordEncoder.encode(studentDTO.getSPw()));

		Student student = modelMapper.map(studentDTO, Student.class);
		return modelMapper.map(studentRepository.save(student), StudentDTO.class);
	}

	@Override
	public Optional<StudentDTO> getStudentById(String sId) {
		return studentRepository.findById(sId)
				.map(student -> modelMapper.map(student, StudentDTO.class));
	}

	@Override
	public StudentDTO updateStudent(String sId, StudentDTO studentDTO) {
		Student student = studentRepository.findById(sId)
				.orElseThrow(() -> new EntityNotFoundException("Student with id " + sId + " not found"));

		// 학번 중복 확인 (다른 학생과 중복되는 경우)
		if (!student.getSId().equals(studentDTO.getSId()) && studentRepository.existsById(studentDTO.getSId())) {
			throw new IllegalArgumentException("이미 중복된 학번이 존재합니다: " + studentDTO.getSId());
		}

		// 이메일 중복 확인 (다른 학생과 중복되는 경우)
		if (!student.getSEmail().equals(studentDTO.getSEmail()) && studentRepository.existsBysEmail(studentDTO.getSEmail())) {
			throw new IllegalArgumentException("이미 중복된 이메일이 존재합니다: " + studentDTO.getSEmail());
		}

		modelMapper.map(studentDTO, student);
		return modelMapper.map(studentRepository.save(student), StudentDTO.class);
	}

	@Override
	public void deleteStudent(String sId) {
		// 학생 존재 확인
		Student student = studentRepository.findById(sId)
				.orElseThrow(() -> new IllegalArgumentException("학생을 찾을 수 없습니다."));

		// 해당 학생의 등록금 고지서 삭제
		List<TuitionInvoiceUpload> invoices = uploadRepository.findByStudent_sId(sId);
		for (TuitionInvoiceUpload invoice : invoices) {
			try {
				// 파일 삭제
				Path path = Path.of(invoice.getFilePath());
				if (Files.exists(path)) {
					Files.delete(path); // 파일 삭제
				}
			} catch (IOException e) {
				throw new IllegalStateException("등록금 고지서 파일 삭제 중 오류 발생: " + e.getMessage());
			}

			// 데이터베이스에서 등록금 고지서 삭제
			uploadRepository.delete(invoice);
		}

		// 학생 삭제
		studentRepository.delete(student);
	}


	@Override
	public Page<StudentDTO> searchStudentsById(String sId, Pageable pageable) {
		return studentRepository.findBysIdContainingIgnoreCase(sId, pageable)
				.map(student -> modelMapper.map(student, StudentDTO.class));
	}

	@Override
	public Page<StudentDTO> searchStudentsByName(String sName, Pageable pageable) {
		return studentRepository.findBysNameContainingIgnoreCase(sName, pageable)
				.map(student -> modelMapper.map(student, StudentDTO.class));
	}

	@Override
	public Page<StudentDTO> searchStudentsByDept(String sDept, Pageable pageable) {
		try {
			// sDept 를 Enum 타입으로 변환
			Dept dept = Dept.valueOf(sDept.toUpperCase()); // 유효하지 않은 값이면 예외 발생
			return studentRepository.findBysDept(dept, pageable) // 변환된 Dept 타입 사용
					.map(student -> modelMapper.map(student, StudentDTO.class));
		} catch (IllegalArgumentException e) {
			// 변환 실패 시 예외 처리
			throw new IllegalArgumentException("유효하지 않은 학부 값입니다.");
		}
	}

	@Override
	public Page<StudentDTO> getAllStudents(Pageable pageable) {
		return studentRepository.findAll(pageable)
				.map(student -> modelMapper.map(student, StudentDTO.class));
	}

}
