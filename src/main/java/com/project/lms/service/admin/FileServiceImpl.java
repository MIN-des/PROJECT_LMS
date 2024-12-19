package com.project.lms.service.admin;

import com.project.lms.entity.Board;
import com.project.lms.entity.Files;
import com.project.lms.repository.admin.BoardRepository;
import com.project.lms.repository.admin.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl {
  private static final String UPLOAD_DIR = "C:\\upload"; // 파일이 저장될 경로

  private final BoardRepository boardRepository;
  private final FileRepository fileRepository;


  // 파일 업로드 메서드
  public void uploadFile(Long bno, MultipartFile file) throws IOException {
    if (file.isEmpty()) {
      throw new IllegalArgumentException("파일이 비어 있습니다.");
    }

    // 디렉토리가 없으면 생성
    File directory = new File(UPLOAD_DIR);
    if (!directory.exists()) {
      directory.mkdirs();
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
}
