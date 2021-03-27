package com.cityCatTarot.controllers;

import com.cityCatTarot.dto.ErrorResponse;
import com.cityCatTarot.errors.CardNotFoundException;
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
        return new ErrorResponse("Card not found");
    }
}
