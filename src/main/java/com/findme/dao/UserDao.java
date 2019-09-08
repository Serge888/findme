package com.findme.dao;

import com.findme.exception.InternalServerException;
import com.findme.models.User;

import java.util.List;

public interface UserDao {

    User save(User user) throws InternalServerException;
    User update(User user) throws InternalServerException;
    User delete(User user) throws InternalServerException;
    User findById(Long id) throws InternalServerException;
    User findByPhoneNumber(String phoneNumber) throws InternalServerException;
    User findByEmailAddress(String emailAddress) throws InternalServerException;
    List<User> findTaggedUsers(Long userPostedId, List<Long> usersTaggedIdList) throws InternalServerException;

}
