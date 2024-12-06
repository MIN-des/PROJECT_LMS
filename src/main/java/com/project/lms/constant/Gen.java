package com.project.lms.constant;

public enum Gen {
  MALE("남성"),
  FEMALE("여성");

  private final String koreanName;

  Gen(String koreanName) {
    this.koreanName = koreanName;
  }

  public String getKoreanName() {
    return koreanName;
  }
}
