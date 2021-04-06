package com.cityCatTarot.errors;

/**
 * 등록되지 않은 이메일로 로그인 시도 시 발생한 로그인 실패 예외.
 */
public class LoginFailWithNotFoundEmailException extends RuntimeException{
    public LoginFailWithNotFoundEmailException(String email) {
        super("email is not found :  " + email);
    }
}
