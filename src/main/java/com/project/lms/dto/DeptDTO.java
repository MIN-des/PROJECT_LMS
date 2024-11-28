package com.project.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeptDTO {

    private String deptId;
    private String deptName;
    private String office;
}
