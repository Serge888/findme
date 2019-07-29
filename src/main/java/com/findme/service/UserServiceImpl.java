package com.findme.service;

import com.findme.Util.UtilString;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.models.User;
import com.findme.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User save(User user) throws InternalServerException {
        if (user == null
                || user.getPhone() == null
                || user.getEmailAddress() == null) {
             throw new BadRequestException("User can't be saved without Phone Number or Email Address.");
        }
        String emailAddress = user.getEmailAddress().trim();
        if (!UtilString.isEmail(emailAddress)) {
            throw new BadRequestException("Email Address contains a mistake");
        }
        String realPhoneNumber = UtilString.phoneChecker(user.getPhone());
        if (realPhoneNumber == null) {
            throw new BadRequestException("Phone Number contains a mistake");
        }

        if (userDao.findByEmailAddress(emailAddress) != null) {
            throw new BadRequestException("User with email address: " + emailAddress + " already exists.");
        }
        if (userDao.findByPhoneNumber(realPhoneNumber) != null) {
            throw new BadRequestException("User with phone number: " + realPhoneNumber + " already exists.");
        }
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
    public User findById(Long id) throws InternalServerException {
        return userDao.findById(id);
    }

    @Override
    public User findByPhoneNumber(String phoneNumber) throws InternalServerException {
        return userDao.findByPhoneNumber(phoneNumber);
    }

    @Override
    public User findByEmailAddress(String emailAddress) throws InternalServerException {
        return userDao.findByEmailAddress(emailAddress);
    }

}
