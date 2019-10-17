package com.findme.exception;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = Logger.getLogger(GlobalExceptionHandler.class);

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity handlerLoginException(Exception e) {
        logger.error("BadRequestException handler executed." + e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
        return new ResponseEntity<>("BadRequestException handler executed. " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(InternalServerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handlerInternalServerException(Exception e) {
        logger.error("InternalServerException handler executed." + e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
        ModelAndView modelAndView = new ModelAndView("500");
        modelAndView.addObject("exception", e.getMessage());
        return modelAndView;
    }


    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handlerNotFoundException(Exception e) {
        logger.error("NotFoundException handler executed." + e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
        ModelAndView modelAndView = new ModelAndView("404");
        modelAndView.addObject("exception", e.getMessage());
        return modelAndView;
    }



    @ExceptionHandler(AccessForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ModelAndView handlerAccessForbiddenException(Exception e) {
        logger.error("AccessForbiddenException handler executed."  + e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
        ModelAndView modelAndView = new ModelAndView("403");
        modelAndView.addObject("exception", e.getMessage());
        return modelAndView;
    }


}
