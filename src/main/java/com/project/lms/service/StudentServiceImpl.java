package com.project.lms.service;

import com.project.lms.constant.Dept;
import com.project.lms.dto.ProfessorDTO;
import com.project.lms.dto.StudentDTO;
import com.project.lms.entity.Student;
import com.project.lms.repository.StudentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public StudentDTO createStudent(StudentDTO studentDTO) {
		if (studentRepository.existsById(studentDTO.getSId())) {
			throw new IllegalArgumentException("이미 중복된 학번이 존재합니다.");
		}
		if (studentRepository.existsBysEmail(studentDTO.getSEmail())) {
			throw new IllegalArgumentException("이미 중복된 이메일이 존재합니다.");
		}
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
		studentRepository.deleteById(sId);
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

//	@Override
//	public Page<StudentDTO> searchStudentsByDept(String sDept, Pageable pageable) {
//		Dept dept = Dept.valueOf(sDept.toUpperCase()); // Enum 변환
//		return StudentRepository.findBysDept(dept, pageable)
//				.map(student -> modelMapper.map(student, StudentDTO.class));
//	}


	@Override
	public Page<StudentDTO> getAllStudents(Pageable pageable) {
		return studentRepository.findAll(pageable)
				.map(student -> modelMapper.map(student, StudentDTO.class));
	}
}