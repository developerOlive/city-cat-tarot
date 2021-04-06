package com.cityCatTarot.errors;

/**
 * 비밀번호 암호화 인코딩 실패 예외.
 */
public class EncoderFailException extends RuntimeException{
    public EncoderFailException(String email) {
        super("encoderFailException - email: " + email);
    }
}
