package com.project.lms;

import com.project.lms.entity.AllBoard;
import com.project.lms.repository.admin.AllBoardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class AllBoardInsertTest {

    @Autowired
    private AllBoardRepository allBoardRepository;

    @Test
    public void testInsertAllBoard() {
        AllBoard allBoard = new AllBoard();
        allBoard.setAllTitle("테스트 제목");
        allBoard.setAllContent("테스트 내용");
        allBoard.setAllWriter("테스트 작성자");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        AllBoard savedAllBoard = allBoardRepository.save(allBoard);

        assertNotNull(savedAllBoard.getAllBno());
        assertNotNull(savedAllBoard.getRegDate());
        assertNotNull(savedAllBoard.getModDate());
    }
}