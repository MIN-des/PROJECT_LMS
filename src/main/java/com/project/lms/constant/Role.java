package com.project.lms.constant;

public enum Role {
  ROLE_ADMIN("관리자"),
  ROLE_STUDENT("학생"),
  ROLE_PROFESSOR("교수");

  private final String koreanName;

  Role(String koreanName) {
    this.koreanName = koreanName;
  }

  public String getKoreanName() {
    return koreanName;
  }
}
