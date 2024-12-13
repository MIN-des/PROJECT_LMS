package com.project.lms.exception;

// 잔여인원 0보다 작은 경우 오류 메시지 발생시키는 클래스
public class OutOfRestNumException extends RuntimeException {
    public OutOfRestNumException(String message) {
        super(message);
    }
}
