package com.project.lms.dto;

import com.project.lms.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {

    private String mid;
    private String mname;
    private String password;
    private String email;
    private String phone;
    private Role role;
}
