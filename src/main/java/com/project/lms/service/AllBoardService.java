package com.project.lms.service;

import com.project.lms.dto.AllBoardDTO;

public interface AllBoardService {

    Long register(AllBoardDTO allBoardDTO);

    AllBoardDTO readOne(Long allBno);

    void modify(AllBoardDTO allBoardDTO);

    void remove(Long allBno);
}