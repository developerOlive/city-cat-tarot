package com.cityCatTarot.controllers;

import com.cityCatTarot.dto.ErrorResponse;
import com.cityCatTarot.errors.AccessDeniedException;
import com.cityCatTarot.errors.CardNotFoundException;
import com.cityCatTarot.errors.EncoderFailException;
import com.cityCatTarot.errors.LoginFailException;
import com.cityCatTarot.errors.LoginFailWithNotFoundEmailException;
import com.cityCatTarot.errors.LoginFailWithWrongPwException;
import com.cityCatTarot.errors.UserEmailDuplicationException;
import com.cityCatTarot.errors.UserNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * HTTP 요청 에러 핸들러.
 */
@ResponseBody
@ControllerAdvice
public class ControllerErrorAdvice {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CardNotFoundException.class)
    public ErrorResponse handleProductNotFound() {
        return new ErrorResponse("Card not found.");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ErrorResponse handleUserNotFound() {
        return new ErrorResponse("User not found.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserEmailDuplicationException.class)
    public ErrorResponse handleUserEmailIsAlreadyExisted() {
        return new ErrorResponse("User's email address is already existed.");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(LoginFailException.class)
    public ErrorResponse handleLoginFailException() {
        return new ErrorResponse("Log-in failed.");
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ErrorResponse handleUserIdNotMatchException() {
        return new ErrorResponse("Access denied. Forbidden.");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ErrorResponse handleLoginFailWithWrongEmailException() {
        return new ErrorResponse("Login failed.");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(LoginFailWithNotFoundEmailException.class)
    public ErrorResponse handleLoginFailWithWrongEmailException2() {
        return new ErrorResponse("Login failed. Email is wrong.");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(EncoderFailException.class)
    public ErrorResponse handleEncoderFailException() {
        return new ErrorResponse("Login failed. Password is wrong.");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(LoginFailWithWrongPwException.class)
    public ErrorResponse handleLoginFailWithWrongPasswordException() {
        return new ErrorResponse("Login failed. Password is wrong.");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponse handleLoginFailWithNoPasswordException() {
        return new ErrorResponse("Login failed. Password is null.");
    }

}
