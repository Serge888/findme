package com.findme.service;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.models.Relationship;
import com.findme.models.User;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface UserService {

    User save(User user) throws InternalServerException;
    User update(User user) throws InternalServerException;
    User delete(User user) throws InternalServerException;
    User findById(Long id) throws NotFoundException, InternalServerException;
    User findByPhoneNumber(String phoneNumber) throws NotFoundException, InternalServerException;
    User findByEmailAddress(String emailAddress) throws NotFoundException, InternalServerException;
    User userLogin(String emailAddress, String password) throws NotFoundException, InternalServerException;
    void viewProfileValidation(HttpSession session, Long profileUserId, Relationship relationship) throws BadRequestException;
    void isUserLoggedIn(HttpSession session, Long userIdFrom) throws BadRequestException;
    List<User> findTaggedUsers(Long userPostedId, List<Long> usersTaggedIdList) throws InternalServerException;
}