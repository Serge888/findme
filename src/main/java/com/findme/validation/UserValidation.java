package com.findme.validation;

import com.findme.exception.BadRequestException;

import javax.servlet.http.HttpSession;

public interface UserValidation {
    void viewProfileValidation(HttpSession session, Long profileUserId) throws BadRequestException;
    void isUserLoggedIn(HttpSession session, String userIdFrom) throws BadRequestException;
}
