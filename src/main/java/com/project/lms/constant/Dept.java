package com.project.lms.constant;

public enum Dept {
  HUMAN("인문"),
  SOCIAL("사회"),
  TECH("공학"),
  ARTS("예체능");

  private final String koreanName;

  Dept(String koreanName) {
    this.koreanName = koreanName;
  }

  public String getKoreanName() {
    return koreanName;
  }
}
