package com.project.lms.service;

import com.project.lms.constant.Dept;
import com.project.lms.dto.ProfessorDTO;
import com.project.lms.entity.Professor;
import com.project.lms.repository.ProfessorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class ProfessorServiceImpl implements ProfessorService {

	@Autowired
	private ProfessorRepository professorRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public ProfessorDTO createProfessor(ProfessorDTO professorDTO) {
		if (professorRepository.existsById(professorDTO.getPId())) {
			throw new IllegalArgumentException("이미 중복된 교직원 번호가 존재합니다.");
		}
		if (professorRepository.existsBypEmail(professorDTO.getPEmail())) {
			throw new IllegalArgumentException("이미 중복된 이메일이 존재합니다.");
		}
		Professor professor = modelMapper.map(professorDTO, Professor.class);
		return modelMapper.map(professorRepository.save(professor), ProfessorDTO.class);
	}

	@Override
	public Optional<ProfessorDTO> getProfessorById(String pId) {
		return professorRepository.findById(pId)
				.map(professor -> modelMapper.map(professor, ProfessorDTO.class));
	}

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

//	@Override
//	public Page<ProfessorDTO> searchProfessorsByDept(String pDept, Pageable pageable) {
//		Dept dept = Dept.valueOf(pDept.toUpperCase()); // Enum 변환
//		return professorRepository.findBypDept(dept, pageable)
//				.map(professor -> modelMapper.map(professor, ProfessorDTO.class));
//	}

	@Override
	public Page<ProfessorDTO> getAllProfessors(Pageable pageable) {
		return professorRepository.findAll(pageable)
				.map(professor -> modelMapper.map(professor, ProfessorDTO.class));
	}
}