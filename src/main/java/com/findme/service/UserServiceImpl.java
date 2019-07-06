package com.findme.service;

import com.findme.models.User;
import com.findme.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;


@Transactional
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User save(User user) {
        user.setDateRegistered(new Date());
        return userDao.save(user);
    }

    @Override
    public User update(User user) {
        return userDao.update(user);
    }

    @Override
    public User delete(Long id) {
        return userDao.delete(id);
    }

    @Override
    public User findById(Long id) {
        return userDao.findById(id);
    }
}
