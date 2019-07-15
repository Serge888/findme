package com.findme.service;

import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.models.User;
import com.findme.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;


@Transactional
@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User save(User user) throws InternalServerException {
        user.setDateRegistered(new Date());
        return userDao.save(user);
    }

    @Override
    public User update(User user) throws InternalServerException {
        return userDao.update(user);
    }

    @Override
    public User delete(User user) throws InternalServerException {
        return userDao.delete(user);
    }

    @Override
    public User findById(Long id) throws NotFoundException, InternalServerException {
        return userDao.findById(id);
    }
}
