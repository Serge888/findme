package com.findme.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Something was wrong with your request")
    @ExceptionHandler(BadRequestException.class)
    public void handlerBadRequestException() {
        logger.error("BadRequestException handler executed.");
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Something was wrong with server")
    @ExceptionHandler(InternalServerException.class)
    public void handlerInternalServerException() {
        logger.error("InternalServerException handler executed.");
    }


    @ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Access is forbidden.")
    @ExceptionHandler(AccessForbiddenException.class)
    public void handlerAccessForbiddenException() {
        logger.error("AccessForbiddenException handler executed.");
    }


    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The information that you have been looking for was not found.")
    @ExceptionHandler(NotFoundException.class)
    public void handlerNotFoundException() {
        logger.error("NotFoundException handler executed.");
    }



}
