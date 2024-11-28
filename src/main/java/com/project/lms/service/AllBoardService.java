package com.project.lms.service.admin;

import com.project.lms.dto.AllBoardDTO;
import com.project.lms.dto.PageRequestDTO;
import com.project.lms.dto.PageResponseDTO;

public interface AllBoardService {

    Long register(AllBoardDTO allBoardDTO);

    AllBoardDTO readOne(Long allBno);

    void modify(AllBoardDTO allBoardDTO);

    void remove(Long allBno);

    PageResponseDTO<AllBoardDTO> list(PageRequestDTO pageRequestDTO);
}