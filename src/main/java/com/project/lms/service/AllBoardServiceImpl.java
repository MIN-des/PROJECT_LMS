package com.project.lms.service;

import com.project.lms.dto.AllBoardDTO;
import com.project.lms.entity.AllBoard;
import com.project.lms.repository.AllBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class AllBoardServiceImpl implements AllBoardService {

    private final ModelMapper modelMapper;

    private final AllBoardRepository allBoardRepository;

    @Override
    public Long register(AllBoardDTO allBoardDTO) {
        AllBoard allBoard = modelMapper.map(allBoardDTO, AllBoard.class);

        Long allBno = allBoardRepository.save(allBoard).getAllBno();

        return allBno;
    }

    @Override
    public AllBoardDTO readOne(Long allBno) {
        return null;
    }

    @Override
    public void modify(AllBoardDTO allBoardDTO) {

    }

    @Override
    public void remove(Long allBno) {

    }
}
