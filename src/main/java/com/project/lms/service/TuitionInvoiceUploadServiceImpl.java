package com.project.lms.service;

import com.project.lms.controller.StudentController;
import com.project.lms.dto.StudentDTO;
import com.project.lms.dto.TuitionInvoiceUploadDTO;
import com.project.lms.entity.Student;
import com.project.lms.entity.TuitionInvoiceUpload;
import com.project.lms.repository.StudentRepository;
import com.project.lms.repository.TuitionInvoiceUploadRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TuitionInvoiceUploadServiceImpl implements TuitionInvoiceUploadService {

	// 필드 선언
	private static final Logger log = LoggerFactory.getLogger(StudentController.class);
	private final TuitionInvoiceUploadRepository uploadRepository;
	private final StudentRepository studentRepository;
	private final ModelMapper modelMapper;

	// 현재 경로 가져오기
	private final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/invoices";

	// 의존성 주입
	public TuitionInvoiceUploadServiceImpl(TuitionInvoiceUploadRepository uploadRepository,
																				 StudentRepository studentRepository,
																				 ModelMapper modelMapper) {
		this.uploadRepository = uploadRepository;
		this.studentRepository = studentRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public void uploadInvoice(String sId, MultipartFile file) throws Exception {
		// 학생 존재 확인
		Student student = studentRepository.findById(sId)
				.orElseThrow(() -> new IllegalArgumentException("해당 학번을 가진 학생이 존재하지 않습니다."));

		// 업로드 디렉토리 생성
		File directory = new File(UPLOAD_DIR);
		if (!directory.exists() && !directory.mkdirs()) {
			throw new IOException("디렉토리를 생성할 수 없습니다: " + UPLOAD_DIR);
		}

		// 파일 유효성 검사
		if (file.isEmpty()) {
			throw new IllegalArgumentException("업로드된 파일이 비어 있습니다.");
		}

		// 고유한 파일 이름 생성, 파일 이름 앞에 날짜 생성
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
		String date = LocalDateTime.now().format(formatter);

		// 파일명 생성
		String fileName = date + "_" + file.getOriginalFilename();
		Path filePath = Path.of(UPLOAD_DIR, fileName);

		// 파일 저장
		Files.copy(file.getInputStream(), filePath);

		// 엔티티 저장
		TuitionInvoiceUpload invoice = new TuitionInvoiceUpload();
		invoice.setStudent(student);
		invoice.setFileName(fileName);
		invoice.setFilePath(filePath.toString());
		invoice.setUploadDate(LocalDateTime.now());
		uploadRepository.save(invoice);
	}

	// 학번으로 invoiceList 조회
	@Override
	public List<TuitionInvoiceUploadDTO> getInvoicesByStudentId(String sId) {
		List<TuitionInvoiceUpload> uploads = uploadRepository.findByStudent_sId(sId);
		return uploads.stream().map(upload -> {
			TuitionInvoiceUploadDTO dto = new TuitionInvoiceUploadDTO();
			dto.setTId(upload.getTId()); // tId 설정
			dto.setSId(upload.getStudent().getSId()); // 학번 설정
			dto.setSName(upload.getStudent().getSName()); // 이름 설정
			dto.setFileName(upload.getFileName());
			dto.setUploadDate(upload.getUploadDate());
			return dto;
		}).collect(Collectors.toList());
	}

	public boolean isInvoiceOwnedByStudent(Long tId, String sId) {
		return uploadRepository.existsBytIdAndStudent_sId(tId, sId);
	}

	// 파일 다운로드
	@Override
	// 파일 데이터를 바이트(byte) 배열로 반환
	public byte[] downloadInvoice(Long tId) throws Exception {
		// 잘못된 번호로 메소드를 호출하지 못하게 막음
		if (tId == null || tId <= 0) {
			throw new IllegalArgumentException("tId가 유효하지 않습니다.");
		}

		// 등록금 고지서를 데이터베이스에서 조회
		TuitionInvoiceUpload invoice = uploadRepository.findById(tId)
				.orElseThrow(() -> new IllegalArgumentException("해당 등록금 고지서를 찾을 수 없습니다."));

		// 엔티티의 filePath 필드를 사용해 해당 파일의 경로를 만듦, 실제로 존재하는지 확인
		Path path = Path.of(invoice.getFilePath());
		log.info("Attempting to download file from path: {}", path);
		if (!Files.exists(path)) {
			throw new IllegalArgumentException("파일을 찾을 수 없습니다. 경로: " + invoice.getFilePath());
		}
		// 지정된 경로의 파일을 바이트 배열 형태로 읽음
		return Files.readAllBytes(path);
	}

	// 관리자가 학생 아이디 조회
	@Override
	public StudentDTO getStudentById(String sId) {
		var student = studentRepository.findById(sId)
				.orElseThrow(() -> new IllegalArgumentException("학생 정보를 찾을 수 없습니다."));
		return modelMapper.map(student, StudentDTO.class);
	}

	// 등록금 고지서 삭제
	@Override
	public void deleteInvoice(Long tId) throws Exception {
		// 데이터베이스에서 등록금 고지서 조회
		TuitionInvoiceUpload invoice = uploadRepository.findById(tId)
				.orElseThrow(() -> new IllegalArgumentException("해당 고지서를 찾을 수 없습니다."));

		// 파일 경로 확인 및 삭제
		Path path = Path.of(invoice.getFilePath());
		if (Files.exists(path)) {
			try {
				Files.delete(path); // 파일 삭제
				System.out.println("파일 삭제 성공: " + path);
			} catch (IOException e) {
				System.err.println("파일 삭제 중 오류 발생: " + e.getMessage());
				throw new IOException("파일을 삭제할 수 없습니다. 경로: " + path);
			}
		} else {
			System.out.println("파일이 존재하지 않습니다: " + path);
		}
		// 데이터베이스에서 고지서 삭제
		uploadRepository.delete(invoice);
	}

	public TuitionInvoiceUpload getInvoiceById(Long tId) {
		return uploadRepository.findById(tId)
				.orElseThrow(() -> new IllegalArgumentException("해당 등록금 고지서를 찾을 수 없습니다."));
	}
}
