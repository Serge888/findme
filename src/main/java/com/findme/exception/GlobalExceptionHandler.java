package com.findme.exception;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
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
    public ModelAndView handlerBadRequestException(Exception e) {
        System.err.println("BadRequestException handler executed.");
        System.err.println(e.getMessage());
        System.err.println(Arrays.toString(e.getStackTrace()));
        logger.error("BadRequestException handler executed.");
        ModelAndView modelAndView = new ModelAndView("400");
        modelAndView.addObject("exception", e.getMessage());
        return modelAndView;
    }


    @ExceptionHandler(InternalServerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handlerInternalServerException(Exception e) {
        System.err.println("InternalServerException handler executed.");
        System.err.println(e.getMessage());
        System.err.println(Arrays.toString(e.getStackTrace()));
        logger.error("InternalServerException handler executed.");
        ModelAndView modelAndView = new ModelAndView("500");
        modelAndView.addObject("exception", e.getMessage());
        return modelAndView;
    }


    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handlerNotFoundException(Exception e) {
        System.err.println("NotFoundException handler executed.");
        System.err.println(e.getMessage());
        System.err.println(Arrays.toString(e.getStackTrace()));
        logger.error("NotFoundException handler executed.");
        ModelAndView modelAndView = new ModelAndView("404");
        modelAndView.addObject("exception", e.getMessage());
        return modelAndView;
    }



    @ExceptionHandler(AccessForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ModelAndView handlerAccessForbiddenException(Exception e) {
        System.err.println("AccessForbiddenException handler executed.");
        System.err.println(e.getMessage());
        System.err.println(Arrays.toString(e.getStackTrace()));
        logger.error("AccessForbiddenException handler executed.");
        ModelAndView modelAndView = new ModelAndView("403");
        modelAndView.addObject("exception", e.getMessage());
        return modelAndView;
    }


}
