package com.project.lms.config;

import com.project.lms.dto.ProfessorDTO;
import com.project.lms.dto.StudentDTO;
import com.project.lms.entity.Professor;
import com.project.lms.entity.Student;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RootConfig {
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();

		// StudentDTO와 Student 간의 명시적 매핑
		modelMapper.typeMap(StudentDTO.class, Student.class).addMappings(mapper -> {
			mapper.map(StudentDTO::getSId, Student::setSId);
			mapper.map(StudentDTO::getSName, Student::setSName);
			mapper.map(StudentDTO::getSPw, Student::setSPw);
			mapper.map(StudentDTO::getSTel, Student::setSTel);
			mapper.map(StudentDTO::getSAdd, Student::setSAdd);
			mapper.map(StudentDTO::getSBirth, Student::setSBirth);
			mapper.map(StudentDTO::getSEmail, Student::setSEmail);
			mapper.map(StudentDTO::getGrade, Student::setGrade);
			mapper.map(StudentDTO::getSGen, Student::setSGen);
			mapper.map(StudentDTO::getSDept, Student::setSDept);
			mapper.map(StudentDTO::getRole, Student::setRole);
		});

		// ProfessorDTO와 Professor 간의 명시적 매핑
		modelMapper.typeMap(ProfessorDTO.class, Professor.class).addMappings(mapper -> {
			mapper.map(ProfessorDTO::getPId, Professor::setPId);
			mapper.map(ProfessorDTO::getPName, Professor::setPName);
			mapper.map(ProfessorDTO::getPPw, Professor::setPPw);
			mapper.map(ProfessorDTO::getPTel, Professor::setPTel);
			mapper.map(ProfessorDTO::getPAdd, Professor::setPAdd);
			mapper.map(ProfessorDTO::getPBirth, Professor::setPBirth);
			mapper.map(ProfessorDTO::getPEmail, Professor::setPEmail);
			mapper.map(ProfessorDTO::getYear, Professor::setYear);
			mapper.map(ProfessorDTO::getPGen, Professor::setPGen);
			mapper.map(ProfessorDTO::getPDept, Professor::setPDept);
			mapper.map(ProfessorDTO::getRole, Professor::setRole);
		});

		// ModelMapper 기본 설정
		modelMapper.getConfiguration()
				.setMatchingStrategy(MatchingStrategies.STRICT) // 엄격한 매칭 전략
				.setFieldMatchingEnabled(true) // 필드 이름 매칭 활성화
				.setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

		return modelMapper;
	}
}