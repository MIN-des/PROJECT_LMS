package com.project.lms.controller.board;

import com.project.lms.dto.board.RegisterFormDTO;
import com.project.lms.entity.Board;
import com.project.lms.entity.Files;
import com.project.lms.service.admin.BoardServiceImpl;
import com.project.lms.service.admin.FileServiceImpl;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;


@RequiredArgsConstructor
@Controller
public class BoardController {
  private final BoardServiceImpl boardServiceImpl;
  private final FileServiceImpl fileService;

  @Value("${file.upload-dir}")
  private String uploadDir;  // 파일 업로드 경로 설정 (application.properties에서 설정)

  // 게시글 목록 페이지
  @GetMapping("/board/list")
  public String list(Model model,
                     @RequestParam(value = "page", defaultValue = "0") int page,
                     @RequestParam(value = "searchType", required = false) String searchType,
                     @RequestParam(value = "keyword", required = false) String keyword,
                     @RequestParam(value = "startDate", required = false) String startDate, // 시작 날짜 추가
                     @RequestParam(value = "endDate", required = false) String endDate) { // 종료 날짜 추가) {


    Page<Board> paging;

    // 날짜 범위와 키워드 검색 조건 처리
    if ((startDate == null || startDate.isEmpty()) && (endDate == null || endDate.isEmpty())) {
      // 날짜가 없을 때 기본 검색 처리
      if (keyword == null || keyword.trim().isEmpty()) {
        paging = boardServiceImpl.getList(page); // 검색어가 없을 때 전체 목록
      } else {
        switch (searchType != null ? searchType.toLowerCase() : "all") {
          case "title":
            paging = boardServiceImpl.searchByTitle(keyword, PageRequest.of(page, 10));
            break;
          case "content":
            paging = boardServiceImpl.searchByContent(keyword, PageRequest.of(page, 10));
            break;
          case "writer":
            paging = boardServiceImpl.searchByWriter(keyword, PageRequest.of(page, 10));
            break;
          case "all":
          default:
            paging = boardServiceImpl.searchAll(keyword, page);
            break;
        }
      }
    } else {
      // 날짜가 제공되었을 경우 날짜 필터링 처리
      LocalDate start = (startDate != null && !startDate.isEmpty()) ? LocalDate.parse(startDate) : null;
      LocalDate end = (endDate != null && !endDate.isEmpty()) ? LocalDate.parse(endDate) : null;

      // 검색 및 페이징 처리
      Page<Board> pagings = boardServiceImpl.filterByDateAndKeyword(start, end, searchType, keyword, page);
      if (pagings == null) {
        pagings = Page.empty(); // 결과가 없을 경우 빈 페이지 반환
      }

      // 페이지 네이션 계산
      int totalPages = pagings.getTotalPages();
      int currentBlock = page / 5; // 5개의 페이지를 한 블록으로 계산
      int startPage = currentBlock * 5; // 현재 블록의 시작 페이지
      int endPage = Math.min(startPage + 4, totalPages - 1); // 현재 블록의 마지막 페이지 번호
      //   int startPage = Math.max(currentBlock * 5, 0); // 현재 블록의 시작 페이지
      //    int endPage = Math.min(startPage + 4, Math.max(totalPages - 1, 0)); // 현재 블록의 끝 페이지

      // 모델에 데이터 추가
      model.addAttribute("paging", pagings);
      model.addAttribute("searchType", searchType);
      model.addAttribute("keyword", keyword);
      model.addAttribute("startDate", startDate);
      model.addAttribute("endDate", endDate);
      model.addAttribute("startPage", startPage);
      model.addAttribute("endPage", endPage);

      return "admin/board/list";
    }

    // 페이징 관련 정보 계산
    int totalPages = paging.getTotalPages();
    int currentBlock = page / 5; // 5개의 페이지 번호로 묶기
    int startPage = currentBlock * 5; // 현재 블록의 시작 페이지
    int endPage = Math.min(startPage + 4, totalPages - 1); // 현재 블록의 마지막 페이지 번호

    // 모델에 데이터 추가
    model.addAttribute("paging", paging);
    model.addAttribute("startPage", startPage);
    model.addAttribute("endPage", endPage);
    model.addAttribute("searchType", searchType); // 검색 타입 유지
    model.addAttribute("keyword", keyword); // 검색 키워드 유지

    return "admin/board/list";// 목록 페이지 반환
  }

  //http://localhost:8081/board/detail/10 검색(없는 allBno번호는 나오지 않으니 주의 필요), 게시글 상세보기
  @GetMapping(value = "/board/detail/{bno}")
  public String detail(Model model, @PathVariable("bno") Long bno) {
    // 조회수 증가
    boardServiceImpl.incrementViews(bno);

    // 게시글 데이터 가져오기
    Board board = this.boardServiceImpl.getBoard(bno);
    model.addAttribute("board", board);
    return "admin/board/detail";
  }

  // 게시글 등록 페이지
  @GetMapping("/admin/board/create")
  public String boardCreate(RegisterFormDTO registerFormDto) {
    //model.addAttribute("registerFormDTO", new RegisterFormDTO());// RegisterFormDTO 객체를 모델에 추가
    return "admin/board/register"; // 등록 페이지 반환
  }

  // 게시글 등록 처리
  @PostMapping("/admin/board/create")
  public String boardCreate(@Valid RegisterFormDTO registerFormDto, BindingResult bindingResult,
                            @RequestParam(value = "file", required = false) MultipartFile[] files) throws IOException {
    if (bindingResult.hasErrors()) {
      return "admin/board/register";
    }

    // Spring Security를 사용하여 현재 로그인된 사용자 ID 가져오기
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String writer = authentication.getName(); // 로그인된 사용자의 ID

    // XSS 공격 방지를 위한 필터링 적용 (이미지 태그 허용)
//    String sanitizedContent = Jsoup.clean(registerFormDto.getContent(),
//            Safelist.relaxed().addTags("img").addAttributes("img", "src", "alt", "title")); // 수정: 이미지 태그와 속성 허용
//    registerFormDto.setContent(sanitizedContent);

    //게시글 생성
    Board newBoard = this.boardServiceImpl.create(registerFormDto.getTitle(), registerFormDto.getContent());
    // 첨부파일 처리
    if (files != null) {
      for (MultipartFile file : files) { // 배열 변수 files를 순회
        if (!file.isEmpty()) { // 각 파일이 비어 있지 않은 경우 처리
          String originalFilename = file.getOriginalFilename();
          String uuid = UUID.randomUUID().toString();
          String savedFileName = uuid + "_" + originalFilename;

          // 파일을 지정된 경로에 저장
          File targetFile = new File(uploadDir + File.separator + savedFileName);
          if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs(); // 디렉터리가 없으면 생성
          }

          file.transferTo(targetFile);

          // DB에 파일 정보 저장
          Files newFile = new Files();
          newFile.setUuid(uuid);
          newFile.setFName(originalFilename);
          newFile.setFPath(targetFile.getAbsolutePath());
          newFile.setBno(newBoard);

          this.fileService.saveFile(newFile);
        }
      }
    }

    return "redirect:/board/list";
  }

  // 이미지 업로드 API
  @PostMapping("/board/upload/image")
  @ResponseBody
  public Map<String, String> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
    // 파일 저장 경로 설정
    String originalFilename = file.getOriginalFilename();
    String uuid = UUID.randomUUID().toString();
    String savedFileName = uuid + "_" + originalFilename;

    File targetFile = new File(uploadDir, savedFileName);
    if (!targetFile.getParentFile().exists()) {
      targetFile.getParentFile().mkdirs();
    }

    // 파일 저장
    file.transferTo(targetFile);

    // 업로드된 파일 URL 반환
    String fileUrl = "/uploaded-images/" + savedFileName;
    return Map.of("url", fileUrl); // 클라이언트에 URL을 반환
  }



  // 게시글 수정 폼
  @GetMapping("/admin/board/detail/{bno}/modify")
  public String modifyForm(@PathVariable Long bno, Model model) {
    Board board = boardServiceImpl.getBoard(bno);

    // 게시글 데이터를 DTO에 담아 폼에 전달
    RegisterFormDTO registerFormDTO = new RegisterFormDTO();
    registerFormDTO.setTitle(board.getTitle());
    registerFormDTO.setContent(board.getContent());

    model.addAttribute("registerFormDTO", registerFormDTO);
    model.addAttribute("bno", bno);

    return "admin/board/modify";
  }

  // 게시글 수정 처리
  @PostMapping("/admin/board/detail/{bno}/modify")
  // 파일 업로드 처리
  public String modify(@PathVariable Long bno, @Valid @ModelAttribute RegisterFormDTO registerFormDTO, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "admin/board/modify";
    }
    boardServiceImpl.modify(bno, registerFormDTO.getTitle(), registerFormDTO.getContent());
    return "redirect:/board/detail/{bno}";
  }

  // 게시글 삭제
  @PostMapping("/admin/board/detail/{bno}/delete")
  public String delete(@PathVariable Long bno) {
    boardServiceImpl.delete(bno); // 서비스에서 삭제 로직 호출
    return "redirect:/board/list"; // 게시글 목록 페이지로 리다이렉트
  }

  // 파일 업로드 처리
  @PostMapping("admin/board/detail/{bno}/upload")
  public String uploadFile(@PathVariable Long bno, @RequestParam("file") MultipartFile file) throws IOException {
    if (file.isEmpty()) {
      return "파일을 선택해주세요.";
    }

    // 파일 이름과 경로 설정
    String originalFilename = file.getOriginalFilename();
    String uuid = UUID.randomUUID().toString();
    String savedFileName = uuid + "_" + originalFilename;

    // 파일을 지정된 경로에 저장
    File targetFile = new File(uploadDir + File.separator + savedFileName);
    file.transferTo(targetFile);

    // DB에 파일 정보 저장
    Board board = boardServiceImpl.getBoard(bno);
    Files files = new Files();
    files.setUuid(uuid);
    files.setFName(originalFilename);
    files.setFPath(targetFile.getAbsolutePath());
    files.setBno(board);

    board.getFileList().add(files);
    boardServiceImpl.save(board);  // 파일 정보 저장 후 board 객체 저장

    return "redirect:/board/detail/" + bno;
  }


  @GetMapping("/board/file/download/{id}")
  public ResponseEntity<Resource> downloadFile(@PathVariable Long id) throws IOException {
    Files file = fileService.getFile(id);
    Path filePath = Paths.get(file.getFPath());
    Resource resource = new UrlResource(filePath.toUri());
    if (!resource.exists() || !resource.isReadable()) {
      throw new IOException("파일을 읽을 수 없거나 존재하지 않습니다.");
    }

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + UriUtils.encode(file.getFName(), "UTF-8"))
            .body(resource);
  }


}
