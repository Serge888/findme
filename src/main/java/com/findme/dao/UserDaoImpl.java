package com.findme.dao;

import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.models.User;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public class UserDaoImpl extends GeneralDao<User> implements UserDao {

    @Override
    public User findById(Long id) throws NotFoundException, InternalServerException {
        User user;
        try {
            user =  entityManager.find(User.class, id);
        } catch (Exception e) {
            throw new InternalServerException("Something went wrong with findById userId = " + id);
        }
        if (user == null) {
            throw new NotFoundException("User id " + id + " was not found");
        }
        return user;
    }
}
