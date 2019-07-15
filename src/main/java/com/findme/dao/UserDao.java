package com.findme.dao;

import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.models.User;

public interface UserDao {

    User save(User user) throws InternalServerException;
    User update(User user) throws InternalServerException;
    User delete(User user) throws InternalServerException;
    User findById(Long id) throws InternalServerException, NotFoundException;
}
