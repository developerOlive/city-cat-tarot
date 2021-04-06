package com.cityCatTarot.errors;

/**
 * 잘못된 패스워드로 로그인 시도 시 발생한 로그인 실패 예외.
 */
public class LoginFailWithWrongPwException extends RuntimeException{
    public LoginFailWithWrongPwException(String email) {
        super("password is wrong :  " + email);
    }
}
