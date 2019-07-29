package com.findme.service;

import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.models.User;

public interface UserService {

    User save(User user) throws InternalServerException;
    User update(User user) throws InternalServerException;
    User delete(User user) throws InternalServerException;
    User findById(Long id) throws NotFoundException, InternalServerException;
    User findByPhoneNumber(String phoneNumber) throws NotFoundException, InternalServerException;
    User findByEmailAddress(String emailAddress) throws NotFoundException, InternalServerException;
}
