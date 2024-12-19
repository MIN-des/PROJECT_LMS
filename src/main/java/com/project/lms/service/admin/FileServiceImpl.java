package com.project.lms.service.admin;

import com.project.lms.entity.Board;
import com.project.lms.entity.Files;
import com.project.lms.repository.admin.BoardRepository;
import com.project.lms.repository.admin.FileRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl {
  private static final String UPLOAD_DIR = System.getProperty("user.dir")
          + File.separator + "uploads"
          + File.separator + "boardFile";
//    private static final String UPLOAD_DIR = "C:\\upload"; // 파일이 저장될 경로

  private final BoardRepository boardRepository;
  private final FileRepository fileRepository;


  // 파일 업로드 메서드
  public void uploadFile(Long bno, MultipartFile file) throws IOException {
    if (file == null || file.isEmpty()) {
      // 파일이 비어있으면 즉시 리턴
      return;
    }

    // 프로젝트 루트 디렉토리 기준의 uploads/boardFile 경로 설정
    File directory = new File(UPLOAD_DIR);
    if (!directory.exists()) {
      if (directory.mkdirs() && directory.mkdirs()) {
        System.out.println("Directory created at: " + UPLOAD_DIR);
      } else {
        throw new IOException("Failed to create directory: " + UPLOAD_DIR);
      }
    }
    // 파일 이름과 경로 설정
    String originalFilename = file.getOriginalFilename();
    String uuid = UUID.randomUUID().toString();
    String savedFileName = uuid + "_" + originalFilename;

    // 파일을 지정된 경로에 저장
    File targetFile = new File(UPLOAD_DIR + File.separator + savedFileName); // 수정: uploadDir -> UPLOAD_DIR
    file.transferTo(targetFile); // 파일을 저장

    // Board 객체 조회
    Board board = boardRepository.findById(bno)
            .orElseThrow(() -> new IllegalArgumentException("Invalid board ID: " + bno));

    // Files 객체 생성
    Files newFile = new Files();
    newFile.setUuid(uuid);
    newFile.setFName(originalFilename);
    newFile.setFPath(targetFile.getAbsolutePath());
    newFile.setBno(board);

    // 파일 정보 저장
    fileRepository.save(newFile);

    // 게시글에 파일 추가
    board.getFileList().add(newFile);
  }

  // 파일 저장 메서드
  public void saveFile(Files file) {
    fileRepository.save(file);
  }

  public Files getFile(Long id) {
    return fileRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid file ID: " + id));
  }

  public void deleteFile(Long fileId) {
    // DB에서 파일 조회
    Files file = fileRepository.findById(fileId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 파일입니다."));

    // 파일 시스템에서 삭제
    File physicalFile = new File(file.getFPath());
    if (physicalFile.exists()) {
      try {
        boolean isDeleted = physicalFile.delete();
        if (!isDeleted) {
          throw new IOException("파일 삭제 실패");
        }
      } catch (IOException e) {
        System.err.println("파일 삭제 오류: " + physicalFile.getAbsolutePath());
        e.printStackTrace();
      }
    }
    // DB에서 삭제
    fileRepository.delete(file);
  }

  public String uploadQuillImage(MultipartFile file) throws IOException {
    String originalFilename = file.getOriginalFilename();
    String uuid = UUID.randomUUID().toString();
    String savedFileName = uuid + "_" + originalFilename;

    File targetFile = new File(UPLOAD_DIR, savedFileName);
    if (!targetFile.getParentFile().exists()) {
      targetFile.getParentFile().mkdirs();
    }

    file.transferTo(targetFile);

    // 파일 경로를 DB에 저장 (QuillImages 테이블)
    Files quillImage = new Files();
    quillImage.setUuid(uuid);
    quillImage.setFName(originalFilename);
    quillImage.setFPath(targetFile.getAbsolutePath());
    fileRepository.save(quillImage);

    return "/uploads/boardFile/" + savedFileName; // 클라이언트에 반환할 URL
  }

  @Transactional
  public void deleteUnusedQuillImages(String content) {
    // 1. Quill 콘텐츠에서 이미지 경로 추출
    Document doc = Jsoup.parse(content);
    Elements images = doc.select("img");
    List<String> usedImagePaths = new ArrayList<>();
    for (Element img : images) {
      String imagePath = img.attr("src");
      if (imagePath.startsWith("/uploads/boardFile")) {
        // 저장된 물리적 파일의 전체 경로를 계산하여 저장
        String fullPath = UPLOAD_DIR + File.separator + imagePath.substring("/uploads/boardFile".length());
        usedImagePaths.add(fullPath);
      }
    }

    // 2. 데이터베이스에서 모든 Quill 이미지 가져오기
    List<Files> allQuillImages = fileRepository.findAllByPathLike(UPLOAD_DIR + "%");

    // 3. 사용되지 않는 이미지 식별 및 삭제
    for (Files file : allQuillImages) {
      String fullPath = file.getFPath();
      if (!usedImagePaths.contains(fullPath)) {
        // 물리적 파일 삭제
        File physicalFile = new File(fullPath);
        if (physicalFile.exists() && !physicalFile.delete()) {
          System.err.println("파일 삭제 실패: " + physicalFile.getAbsolutePath());
        }

        // DB에서 파일 삭제
        fileRepository.delete(file);
      }
    }
  }


  //파일 삭제 전담 메서드
  @Transactional
  public void deleteFilesByBoard(Board board) {
    if (board.getFileList() != null && !board.getFileList().isEmpty()) {
      for (Files file : board.getFileList()) {
        // 파일 삭제
        File physicalFile = new File(file.getFPath());
        if (physicalFile.exists() && !physicalFile.delete()) {
          System.err.println("첨부 파일 삭제 실패: " + physicalFile.getAbsolutePath());
        }

        // DB에서 파일 삭제
        fileRepository.delete(file);
      }
    }
  }
}
