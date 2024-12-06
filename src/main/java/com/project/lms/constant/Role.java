package com.project.lms.constant;

public enum Role {
  ADMIN("관리자"),
  STUDENT("학생"),
  PROFESSOR("교수");

  private final String koreanName;

  Role(String koreanName) {
    this.koreanName = koreanName;
  }

  public String getKoreanName() {
    return koreanName;
  }
}
