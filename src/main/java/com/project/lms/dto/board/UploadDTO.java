package com.project.lms.dto.board;

import com.project.lms.entity.Files;
import lombok.Data;

import java.util.List;

@Data
public class  UploadDTO {
    private List<Files> files;
}


