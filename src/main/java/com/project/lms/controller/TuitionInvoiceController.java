package com.project.lms.controller;

import com.project.lms.dto.TuitionInvoiceUploadDTO;
import com.project.lms.entity.TuitionInvoiceUpload;
import com.project.lms.service.TuitionInvoiceUploadService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("/admin/invoices")
public class TuitionInvoiceController {

	private final TuitionInvoiceUploadService uploadService;

	public TuitionInvoiceController(TuitionInvoiceUploadService uploadService) {
		this.uploadService = uploadService;
	}

	// 관리자 등록금 고지서 등록 페이지
	@GetMapping
	public String invoicePage() {
		return "admin/invoices";
	}

	// 등록금 고지서 업로드
	@PostMapping("/upload")
	public String uploadInvoice(@RequestParam String sId, @RequestParam MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			// 파일 업로드 서비스 호출
			uploadService.uploadInvoice(sId, file);
			redirectAttributes.addFlashAttribute("success", "등록금 고지서가 성공적으로 업로드되었습니다.");
		} catch (IllegalArgumentException e) {
			// 학번이 존재하지 않을 경우
			redirectAttributes.addFlashAttribute("error", "해당 학번을 가진 학생이 존재하지 않습니다.");
		} catch (IOException e) {
			// 파일 처리 중 오류
			redirectAttributes.addFlashAttribute("error", "파일 처리 중 오류가 발생했습니다: " + e.getMessage());
		} catch (Exception e) {
			// 기타 알 수 없는 오류
			redirectAttributes.addFlashAttribute("error", "업로드 중 오류가 발생했습니다.");
		}
		// 업로드 완료 후 리다이렉트
		return "redirect:/admin/invoices/student?sId=" + sId;
	}

	// 등록금 고지서 다운로드
	@GetMapping("/download/{tId}")
	public ResponseEntity<byte[]> downloadInvoice(@PathVariable Long tId) throws Exception {

		// 등록금 고지서 엔티티와 파일 데이터 가져오기
		TuitionInvoiceUpload invoice = uploadService.getInvoiceById(tId);
		byte[] fileData = uploadService.downloadInvoice(tId); // 파일 데이터 가져오기

		// 엔티티에 저장된 파일 이름 가져오기
		String originalFileName = invoice.getFileName();

		// 파일 이름 UTF-8로 인코딩
		String encodedFileName = URLEncoder.encode(originalFileName, StandardCharsets.UTF_8.toString()).replace("+", "%20");

		// 응답 헤더 설정을 위한 객체 생성
		HttpHeaders headers = new HttpHeaders();
		//응답 본문의 컨텐츠 타입을 application/pdf 로 설정, 이를 보고 클라이언트는 응답 데이터가 pdf 파일임을 인식
		headers.setContentType(MediaType.APPLICATION_PDF);
		// ContentDisposition 헤더를 설정하여 파일 다운로드, "등록금고지서.pdf" 클라이언트가 다운로드할 때 표시될 파일 이름
		headers.setContentDispositionFormData("attachment", encodedFileName);

		return ResponseEntity.ok()
				.headers(headers)
				.body(fileData);
	}

	// 학생 정보, 등록금 고지서 리스트 가져오기
	@GetMapping("/student")
	public String getStudentInvoicesByQuery(@RequestParam String sId, Model model) {
		try {
			// 학생 정보 조회
			var student = uploadService.getStudentById(sId);
			model.addAttribute("student", student);

			// 해당 학생의 등록금 고지서 리스트 조회
			List<TuitionInvoiceUploadDTO> invoices = uploadService.getInvoicesByStudentId(sId);
			model.addAttribute("invoices", invoices);

		} catch (IllegalArgumentException e) {
			// 학생이 없는 경우 에러 메시지
			model.addAttribute("error", "해당 학번을 가진 학생이 존재하지 않습니다.");
		}
		model.addAttribute("sId", sId);
		return "admin/invoices";
	}

	// 관리자가 등록금 고지서 삭제
	@PostMapping("/delete/{tId}")
	public String deleteInvoice(@PathVariable Long tId, @RequestParam String sId, RedirectAttributes redirectAttributes) {
		try {
			uploadService.deleteInvoice(tId);
			redirectAttributes.addFlashAttribute("success", "등록금 고지서가 성공적으로 삭제되었습니다.");
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("error", "삭제할 고지서를 찾을 수 없습니다.");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "삭제 중 오류가 발생했습니다.");
		}
		return "redirect:/admin/invoices/student?sId=" + sId;
	}
}
