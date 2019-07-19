package com.findme.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ModelAndView handlerBadRequestException(Exception e) {
        logger.error("BadRequestException handler executed.");
        ModelAndView modelAndView = new ModelAndView("400");
        modelAndView.addObject("exception", e.getMessage());
        return modelAndView;
    }


    @ExceptionHandler(InternalServerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handlerInternalServerException(Exception e) {
        logger.error("InternalServerException handler executed.");
        ModelAndView modelAndView = new ModelAndView("500");
        modelAndView.addObject("exception", e.getMessage());
        return modelAndView;
    }


    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handlerNotFoundException(Exception e) {
        logger.error("NotFoundException handler executed.");
        ModelAndView modelAndView = new ModelAndView("404");
        modelAndView.addObject("exception", e.getMessage());
        return modelAndView;
    }



    @ExceptionHandler(AccessForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ModelAndView handlerAccessForbiddenException(Exception e) {
        logger.error("AccessForbiddenException handler executed.");
        ModelAndView modelAndView = new ModelAndView("403");
        modelAndView.addObject("exception", e.getMessage());
        return modelAndView;
    }


}
